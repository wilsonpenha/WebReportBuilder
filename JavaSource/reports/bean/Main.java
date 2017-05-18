package reports.bean;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import reports.InsTransacoesSistema;
import reports.MenuControl;
import br.com.hwork.servlet.PropertiesManager;

public class Main { 

	private Main beanMain;
	private HttpServletRequest rootDIR;
	private String configFileName = "//WEB-INF//pers_srv.properties";
	private static final String PG_SELECAO = "selecao";
	private static final String PG_PAGINA = "pagina";
	private static final String PG_MENU = "Main menu"; 
	
	private String resourceLink = "jdbc/report_ds";

	// Look at ApplicationResources.txt to see what type of server it is. 
	private String contextType = PropertiesManager.getString("application.contextType");   
	
	public String getResourceLink(){return resourceLink;}
	public String getContextType(){return contextType;}
	
	public Main() {  
		System.out.println(contextType);

		br.com.hwork.persistencia.ServerContext.setResourceLink(resourceLink);
		br.com.hwork.persistencia.ServerContext.setContextType(contextType);
	}

	/*-------------------------------------------------------------------------
	                    Atributos 
	-------------------------------------------------------------------------*/
	private static final String MSG_ACESSO_NEGADO =
		"O usu\uFFFDrio n\uFFFDo est\uFFFD autorizado a acessar a transa\uFFFD\uFFFDo solicitada.";
	private static final String MSG_TRANSACAO_DESABILITADA =
		"A transa\uFFFD\uFFFDo solicitada n\uFFFDo est\uFFFD habilitada.";
	public static final String MSG_SENHA_INVALIDA = "Invalid password!";
	public static final String MSG_USUARIO_INEXISTENTE = "User not found!";
	public static final String MSG_USU_EMP_INEXISTENTE = "Company not found!";
	public static final String LOGIN_PAGE = "-1";
	public static final String MAIN_MENU_PAGE = "0";
	public static final String MAIN_PAGE = "7";
	public static final String PAGINA_CADASTRO = "6";

	private String pageId = "-1";
	private String pageIdPrior = null;
	private String form;
	private String dbAction = "";
	private String message = "";
	private String pageIdAnterior = LOGIN_PAGE;
	public boolean acesso = false;
	private String currentUser = "";
	private String loginSenha = "";
	private Vector privileges;

	/*-------------------------------------------------------------------------
	                    Atributos do menu
	-------------------------------------------------------------------------*/

	/*-------------------------------------------------------------------------
	                    Metodos de Acesso SET
	-------------------------------------------------------------------------*/
	//private void setForm(String form){this.form = "" + form + "body.jsp";}
	public void setForm(String form) {
		this.form = "" + form + "body.jsp";
	}
	public void setPageId(String pageId) {
		this.pageId = pageId; 
	}
	public void setDbAction(String dbAction) {
		this.dbAction = dbAction;
	}
	private void setMessage(String message) {
		this.message = message;
	}
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
	public void setLoginSenha(String loginSenha) {
		this.loginSenha = loginSenha;
	}

	public HttpServletRequest getRootDIR() {
		return rootDIR;
	}
	public void setRootDIR(HttpServletRequest request) throws Exception {
		//System.out.println("*********** Carregando o Servidor *****************");
		rootDIR = request;
		String rootDir = BeanObject.replaceAll(this.rootDIR.getRealPath(""), "\\", "//");
		//System.out.println(rootDir);
		br.com.hwork.persistencia.ServerContext.setConfigurationFileName(rootDir + this.configFileName);
		br.com.hwork.persistencia.ServerContext.setLogFileName(rootDir + "//WEB-INF//persistence.log");
		br.com.hwork.persistencia.ErrorLog.setOut(new FileOutputStream(rootDir + "//WEB-INF//errorLog.log"));
		br.com.hwork.persistencia.ClientContext.setLocalDBManager(true);
		beanMain = this;
	}

	/*-------------------------------------------------------------------------
	                    Metodos de Acesso GET
	-------------------------------------------------------------------------*/
	public String getConfigFileName() {
		return this.configFileName;
	}
	public String getPageId() {
		return this.pageId;
	}
	public String getDbAction() {
		return this.dbAction;
	}
	public String getPageIdPrior() {
		return this.pageIdPrior;
	}
	public String getForm() {
		return this.form;
	}
	public String getMessage() {
		return this.message;
	}
	public String getCurrentUser() {
		return this.currentUser.trim();
	}
	public String getLoginSenha() {
		return this.loginSenha.trim();
	}

	/*-------------------------------------------------------------------------
	                    Metodos de negocio
	-------------------------------------------------------------------------*/
	private boolean isPageIdNull() {
		boolean verifica = false;
		if (this.pageId == null) {
			verifica = true;
		}
		return verifica;
	}

	private String parseCommonName(String loginname) {
		StringTokenizer elements = new StringTokenizer(loginname, ",");
		String firstElement = (String) elements.nextElement();
		String commonName = loginname;
		if (firstElement.startsWith("cn=")) {
			commonName = firstElement.substring(3, firstElement.length()).toUpperCase();
		}
		return commonName;
	}

	public void execute() throws Exception {
		this.message = ""; 
		this.acesso = true;
		//loadConfig(); 
	}

	public Hashtable getParams(HttpServletRequest request) {
		Hashtable params = new Hashtable();
		java.util.Enumeration e = request.getParameterNames();
		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();
			if (!s.equalsIgnoreCase("pageId")) {
				String value = request.getParameter(s);
				/* Essa cr\uFFFDtica foi inclu\uFFFDda em virtude de quando exclu\uFFFDdo um registro no displaylist
				 * e logo em seguida fosse feita uma inclusão ou alteração; ao voltar para o displaylist
				 * pelo link, a tela tenta excluir o registro novamente.
				 * Wilson 08/08/01
				 */
				if (s.equalsIgnoreCase("dbAction")) {
					if (value.equals("1") || value.equals("2") || value.equals("3"))
						value = "0";
				}
				request.getParameter(s);
				params.put(s, value);
				/*
				if ((value != null) &&
				    (!value.trim().equals("")) &&
				    (value.length()<30)){
				  params.put(s, value);
				}
				*/
			}

		}
		return params;
	}

	public String getParametros(HttpSession session, String tipo, Double trsNuTransacaoSistema) {
		StringBuffer parametros = new StringBuffer();
		Hashtable paramPagina =
			(Hashtable) session.getAttribute(tipo + "." + String.valueOf(trsNuTransacaoSistema.intValue()));
		if (paramPagina == null) {
			paramPagina = new Hashtable();
		}
		Enumeration enume = paramPagina.keys();
		while (enume.hasMoreElements()) {
			String paramKey = (String) enume.nextElement();
			String paramValue = (String) paramPagina.get(paramKey);
			parametros.append("&" + paramKey + "=" + paramValue);
		}
		return parametros.toString();
	}

	public InsTransacoesSistema getInsTransacoesSistema(String pageId) throws Exception {
		InsTransacoesSistema insTrs = InsTransacoesSistema.recuperaPorID(Double.valueOf(pageId));
		return insTrs;
	}

	public Vector getUserPrivileges() {
		return this.privileges;
	}

	public String printEntry(String text, String url) {
		return "<TR><TD width=\"100%\"  class=\"items\" onClick=\"controlemenu(this,'"
			+ url
			+ "')\"  background=\"../common/images/Blank_btn.jpg\">"
			+ text
			+ "</TD></TR>"; 

	}

	public void loadConfig() throws Exception {
		MenuControl menuControl = MenuControl.findByID(new BigDecimal(this.getPageId()));
		//System.out.println(menuControl);
		this.setForm(menuControl.getUrl());
	}

//	public void loadConfig() throws Exception {
//		InsTransacoesSistema trans = InsTransacoesSistema.recuperaPorID(new Double(this.getPageId()));
//		this.setForm(trans.getTrsEdPagina());
//	}

}
