package reports.bean;

import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.struts.action.ActionForm;

import br.com.hwork.gui.html.HTMLComponent;
import br.com.hwork.persistencia.MetaCol;
import br.com.hwork.persistencia.PObject;
import br.com.hwork.persistencia.ServiceLocator;
import br.com.hwork.servlet.PropertiesManager;
 
/**
* Esta classe é responsável pelo controle genérico de recepção de dados
* na postagem de formulários para serviços Java em 3 camadas. O tratamento
* para inserção, alteração, deleção e visualização de listas de objetos
* negociais persistentes (br.com.hwork.persistencia.PObject) é feito automaticamente
* por todo objeto BeanObject, sendo necessário somente a subscrita de alguns
* métodos.
* <br><br>
* A arquitetura de postagem foi montada pensando-se, principalmente, em sistemas Web.
* Além disso, prima-se pela execução das operações citadas acima (Inserção, Alteração,
* Deleção e Seleção de Listas) sobre classes básicas do sistema.
* <br><br>
* Todo controle do BeanObject resume-se a chamada ao método execute(), onde
* será feito um "roteamento" para um conjunto de operações específicas
* em função da ação de banco de dados (action) corrente do BeanObject.<br><br>
* <b><u>
* O que cada subclasse de BeanObject deve fazer é subscrever alguns métodos de
* modo a preencher os atributos principais de BeanObject relativos a operação
* desejada. Os principais atributos de BeanObject são:<br>
* <ul>
*    <li>pobject: Objeto persistente padrão. A classe do mesmo é informada pelo método
* getPobjectInstance(). Este objeto deve ser preenchido através do método selectObject().
*    <li>list: Lista padrão dos objetos persistentes a serem visualizados em grid.
* A lista deve ser preenchida através do método selectList().</li>
* </ul>
* </u></b>
* <br><br>
* Segue abaixo uma listagem das possíveis ações de um BeanObject:
* <br><br><pre>
*  <b><u>ACTION_EXIT</u></b>
*     Saida do processo de execução. Não usado atualmente.
*
*  <b><u>ACTION_NONE (mesmo que ACTION_SELECT_LIST)</u></b>
*     <b>Nenhuma operação</b> deve ser executada pelo Bean. Esta é a ação padrão INICIAL do Bean.
*     O método execute() encara esta ação como uma ACTION_SELECT_LIST.
*
*  <b><u>ACTION_INSERT</u></b>
*     O objeto persistente padrão do bean será <b>inserido</b> no Banco de Dados.
*     Desencadeia as seguintes operações (nesta ordem)
*       beforeInsert();
*       beforePost();
*       beanToObject();
*       insert();
*       afterPost();
*       afterInsert();
*
*  <b><u>ACTION_UPDATE</u></b>
*     O objeto persistente padrão do bean será <b>alterado</b> no Banco de Dados.
*     Desencadeia as seguintes operações (nesta ordem)
*       beforeUpdate();
*       beforePost();
*       beanToObject();
*       update();
*       afterPost();
*       afterUpdate();
*
*  <b><u>ACTION_DELETE</u></b>
*     <u>Todos</u> os objetos da pagina que foram selecionados (representados pela variavel objIDs)
*     serão <b>deletados</b> do Banco de Dados.
*     Desencadeia as seguintes operações (nesta ordem) <b>para cada objeto selecionado</b>
*       beforeDelete();
*       beanToObject();
*       selectObject(idValues);
*       delete();
*       afterDelete();
*       afterUpdate();
*
*  <b><u>ACTION_SELECT_OBJECT</u></b>
*     O objeto persistente padrão do bean será <b>selecionado</b>. Após a seleção,
*     os dados do bean referentes também presentes no objeto negocial serão preenchidos.
*     Desencadeia as seguintes operações (nesta ordem) para cada objeto selecionado
*       beforeSelectObject();
*       selectObject(idValues);
*       objectToBean();
*       afterSelectObject();
*
*  <b><u>ACTION_SELECT_LIST</u></b>
*     A lista de objetos negociais padrão do bean será <b>preenchida</b>.
*     Desencadeia as seguintes operações (nesta ordem)
*       beforeSelectList();
*       selectList();
*       afterSelectList();
*
*  <b><u>ACTION_RECOVER</u></b>
*     A operação em questão será <b>cancelada</b>. Não usado atualmente.
*     Desencadeia as seguintes operações (nesta ordem)
*       beforeRecover();
*       beanToObject();
*       recover();
*       afterRecover();
*
*  <b><u>ACTION_NEXT</u></b>
*     O contador de página será incrementado e o próximo bloco de objetos será
*     visualizado no grid. Esta ação é usada para navegação de páginas em grids.
*     Desencadeia as seguintes operações (nesta ordem)
*       beforeScroll();
*       next();
*       afterScroll();
*
*  <b><u>ACTION_PRIOR</u></b>
*     O contador de página será decrementado e o bloco anterior de objetos será
*     visualizado no grid. Esta ação é usada para navegação de páginas em grids.
*     Desencadeia as seguintes operações (nesta ordem)
*       beforeScroll();
*       prior();
*       afterScroll();
*
*  <b><u>ACTION_ERROR</u></b>
*     O contador de página será decrementado e o bloco anterior de objetos será
*     visualizado no grid. Esta ação é usada para navegação de páginas em grids.
*     Desencadeia as seguintes operações (nesta ordem)
*     Tratar erros. Não usada atualmente.
*
*  <b><u>ACTION_FORM_INSERT</u></b>
*     O formulário de inserção será colocado como formulário a ser mostrado.
*     Quando o JSP efetuar chamada ao método getBeanForm(), para a inclusão de
*     um formulário junto ao formulário padrão (DEFAULT_FORM), será retornado
*     o formulário de inserção (FORM_INSERT).
*     Desencadeia as seguintes operações (nesta ordem)
*       setBeanForm(this.getFormInsert());
*
*  <b><u>ACTION_FORM_UPDATE</u></b>
*     O formulário de alteração será colocado como formulário a ser mostrado.
*     Quando o JSP efetuar chamada ao método getBeanForm(), para a inclusão de
*     um formulário junto ao formulário padrão (DEFAULT_FORM), será retornado
*     o formulário de alteração (FORM_UPDATE).
*     Desencadeia as seguintes operações (nesta ordem)
*           dbAction = ACTION_SELECT_OBJECT;
*           execute();
*           setBeanForm(getFormUpdate());
*     Obs: como pode ser visto, a ação de seleção de um objeto é executada
*     antes de se mostrar o formulário de alteração
*
*  <b><u>ACTION_FORM_DELETE</u></b>
*     O formulário de deleção será colocado como formulário a ser mostrado.
*     Quando o JSP efetuar chamada ao método getBeanForm(), para a inclusão de
*     um formulário junto ao formulário padrão (DEFAULT_FORM), será retornado
*     o formulário de deleção (FORM_DELETE).
*     Desencadeia as seguintes operações (nesta ordem)
*       setBeanForm(getFormDelete());
*
*  <b><u>ACTION_FORM_DISPLAY_LIST</u></b>
*     O formulário de visualiação da lista de objetos será colocado como formulário
*     a ser mostrado.
*     Quando o JSP efetuar chamada ao método getBeanForm(), para a inclusão de
*     um formulário junto ao formulário padrão (DEFAULT_FORM), será retornado
*     o formulário de lista de objetos (FORM_DISPLAY_LIST).
*     Desencadeia as seguintes operações (nesta ordem)
*       setBeanForm(getFormDisplayList());
*
*  <b><u>ACTION_FORM_SEARCH</u></b>
*     O formulário de pesquisa onde serão informados dados para uma pesquisa
*     a ser mostrado.
*     Quando o JSP efetuar chamada ao método getBeanForm(), para a inclusão de
*     um formulário junto ao formulário padrão (DEFAULT_FORM), será retornado
*     o formulário de pesquisa (FORM_SEARCH).
*     Desencadeia as seguintes operações (nesta ordem)
*       setBeanForm(getFormSearch());
*
*  <b><u>ACTION_FORM_ERROR</u></b>
*     O formulário de inserção será colocado como formulário a ser mostrado.
*     Quando o JSP efetuar chamada ao método getBeanForm(), para a inclusão de
*     um formulário junto ao formulário padrão (DEFAULT_FORM), será retornado
*     o formulário de erro (FORM_ERROR). Atualmente não está em uso.
*     Desencadeia as seguintes operações (nesta ordem)
*       setBeanForm(getFormError());
*  </pre>
*
*/

public abstract class BeanObject extends ActionForm{

  /*-------------------------------------------------------------------------
                      Atributos
  -------------------------------------------------------------------------*/
  public static String MSG_INSERT_OK = "Record has been inserted Successfully!";
  public static String MSG_UPDATE_OK = "Record has been updated Successfully!";
  public static String MSG_DELETE_OK = "Record(s) has been deleted Successfully!";
//  public static String MSG_RECOVER_OK = "Registro(s) recuperado(s) com sucesso !";

  public static final String DEFAULT_FORM = "body.jsp";
  public static final String FORM_DISPLAY_LIST = "displayList.jsp";
  public static final String FORM_SEARCH = "search.jsp";
  public static final String FORM_INSERT = "insert.jsp";
  public static final String FORM_UPDATE = "update.jsp";
  public static final String FORM_DELETE = "delete.jsp";
  public static final String FORM_ERROR = "error.jsp";

  public static final int ACTION_EXIT = -1;
  public static final int ACTION_NONE = 0;
  public static final int ACTION_INSERT = 1;
  public static final int ACTION_UPDATE = 2;
  public static final int ACTION_DELETE = 3;
  public static final int ACTION_SELECT_OBJECT = 4;
  public static final int ACTION_SELECT_LIST = 5;
  public static final int ACTION_RECOVER = 6;
  public static final int ACTION_NEXT = 7;
  public static final int ACTION_PRIOR = 8;
  public static final int ACTION_ERROR = 9;
  public static final int ACTION_SEARCH = 10;
  public static final int ACTION_UPDATE_EDIT = 11;

  public static final int ACTION_FORM_INSERT = 20;
  public static final int ACTION_FORM_UPDATE = 21;
  public static final int ACTION_FORM_DELETE = 22;
  public static final int ACTION_FORM_DISPLAY_LIST = 23;
  public static final int ACTION_FORM_ERROR = 24;
  public static final int ACTION_FORM_SEARCH = 25;
  public static final int ACTION_FORM_VIEW = 26;

  public static final int ACTION_COPY = 30;

  protected boolean errorSelectList = false;
  private Vector commonFields = null;

  protected String message = "";
  protected PObject pobject = null;
  protected Vector pobjects = null;
  protected Vector list = null;
  protected String[] objIds = new String[] {""};
  protected int blockNumber;
  protected int blockSize;
  protected int dbAction = ACTION_NONE;
  protected String form = DEFAULT_FORM;
  protected PrintStream out = System.out;
  protected String pageId = null;
  protected String pageIdPrior = null;
  protected String pgTable = null;
  protected boolean erro = false;
  protected boolean isFormated = false;

  /**
   * Atributos para o controle de acesso
   */
  protected HttpServletRequest request; // Request dos campo do form search
  protected Vector vAtributos = new Vector(); // Vector com os atributos do workflow do form search
  protected Vector vIds = new Vector();
  private String parametrosPesquisa; // String com as tag´s dos parametros de pesquisa

  /*-------------------------------------------------------------------------
                      Construtores
  -------------------------------------------------------------------------*/
  public BeanObject() {
    this.pobject = this.getPObjectInstance();
    this.pobjects = new Vector();
    this.pobjects.add(this.pobject);
    this.blockNumber = 0;
    this.blockSize = 5;
    this.defineFields();
  }

  /*-------------------------------------------------------------------------
                      Metodos de Acesso SET
  -------------------------------------------------------------------------*/
  public String getDbAction(){return new Integer(this.dbAction).toString();}
  public void setDbAction(String dbAction){this.dbAction = Integer.parseInt(dbAction);}
  public void setOut(OutputStream out){
     // this.out = new PrintStream(out);Log.setOut(out);  Comentado por Alecsander para testes no TCU
     this.out = System.out;
     }
  public String getMessage(){return this.message;}
  public Vector getList(){return this.list;}
  public PObject getPObject(){return this.pobject;}
  public String getBlockNumber(){return new Integer(this.blockNumber).toString();}
  public String getBlockSize(){return new Integer(this.blockSize).toString();}
  public void setBlockNumber(String blockNumber){this.blockNumber = Integer.parseInt(blockNumber);}
  public void setBlockSize(String blockSize){this.blockSize = Integer.parseInt(blockSize);}
  public void setObjIds(String[] objIds){this.objIds = objIds;}
  public String[] getObjIds(){return this.objIds;}
  public void setPageId(String pageId){this.pageId = pageId;}
  public String getPageId(){return this.pageId;}
  public void setPageIdPrior(String pageIdPrior){this.pageIdPrior = pageIdPrior;}
  public String getPageIdPrior(){return this.pageIdPrior;}

  public boolean isEmpty(){return (this.list.size()==0);}
  public boolean isFirstBlock(){return (this.blockNumber == 0);}
  public boolean isLastBlock(){return ((this.blockNumber * this.blockSize  + this.blockSize)>=this.list.size());}
  public String getBeanForm(){return this.form;}
  protected void setBeanForm(String form){this.form = form;}

  /**
   * Metodos de acesso para o controle de acesso
   */
  public HttpServletRequest getRequest() {return this.request;}
  public void setRequest(HttpServletRequest request){this.request = request;}
  public java.lang.String getParametrosPesquisa() {return this.parametrosPesquisa;}
  public void setParametrosPesquisa(java.lang.String parametrosPesquisa){this.parametrosPesquisa = parametrosPesquisa;}

  /*-------------------------------------------------------------------------
                      Metodos de negocio
  -------------------------------------------------------------------------*/

  public void addPObject(PObject pobject) {
    this.pobjects.add(pobject);
  }

  /**
   * Substitui um objeto na lista pobjects.
   */
  public void setPObject(PObject pobjectWork) throws Exception{
    PObject pobject = null;
    for (int i = 0; i < this.pobjects.size(); i++) {
      pobject = (PObject) this.pobjects.elementAt(i);
      String pobjectClassName = pobject.getClass().getName();
      String pobjectWorkClassName = pobjectWork.getClass().getName();
      if (pobjectClassName.equalsIgnoreCase(pobjectWorkClassName)) {
        this.pobjects.setElementAt(pobjectWork, i);
        break;
      }
    }
  }

  /*
  * Retorna um objeto da lista
  */
  public PObject getPObject(PObject pobjectWork) throws Exception{
    PObject pobject = null;
    for (int i = 0; i < this.pobjects.size(); i++) {
      pobject = (PObject) this.pobjects.elementAt(i);
      String pobjectClassName = pobject.getClass().getName();
      String pobjectWorkClassName = pobjectWork.getClass().getName();
      if (pobjectClassName.equalsIgnoreCase(pobjectWorkClassName)) {
        break;
      }
    }
    return pobject;
  }


  /**
  * Relaciona o bean com o objeto persistente verificando quais atributos
  * eles têm em comum
  */
  private void defineFields() {
    this.commonFields = new Vector();
    Vector objFsFields = new Vector();
    Vector objBeanFields = new Vector();
    Class poSuperClasse   = this.pobject.getClass();
    Class beanSuperClasse = this.getClass();
    java.lang.reflect.Field[] objFs = poSuperClasse.getDeclaredFields();
    java.lang.reflect.Field[] beanFs = beanSuperClasse.getDeclaredFields();

    while (poSuperClasse.getName().indexOf("PObject") < 0) {
      for (int i=0; i<objFs.length; i++){
        for (int j=0; j<beanFs.length; j++){
          if (objFs[i].getName().equals(beanFs[j].getName())){
            this.commonFields.addElement(objFs[i]);
            break;
          }
        }
      }
      poSuperClasse = poSuperClasse.getSuperclass();
      beanSuperClasse = beanSuperClasse.getSuperclass();
      objFs = poSuperClasse.getDeclaredFields();
      beanFs = beanSuperClasse.getDeclaredFields();
    }

//    java.lang.reflect.Field[] objFs = this.pobject.getClass().getDeclaredFields();
//    java.lang.reflect.Field[] beanFs = this.getClass().getDeclaredFields();

    // se o Bean e o PObject possuirem atributos de
    // mesmo nome e mesmo tipo entao

//    for (int i=0; i<objFs.length; i++){
//      for (int j=0; j<beanFs.length; j++){
//        if (objFs[i].getName().equals(beanFs[j].getName())){
//          this.commonFields.addElement(objFs[i]);
//          break;
//        }
//      }
//    }
  }

  private String buildGetMethodName(String attrName){return "get" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());}
  private String buildSetMethodName(String attrName){return "set" + attrName.substring(0, 1).toUpperCase() + attrName.substring(1, attrName.length());}

  /**
  * Copia os valores do objeto persistente padrão para o bean.
  */
  protected void objectToBean()
  throws Exception{
  this.pobjects.setElementAt(this.pobject,0);
    Iterator iPobjects = this.pobjects.iterator();
    try {
      while(iPobjects.hasNext()) {
        this.pobject = (PObject) iPobjects.next();
//        PObject poSuperClasse   = this.pobject;
        this.defineFields();
        Enumeration e = this.commonFields.elements();
        while (e.hasMoreElements()){
          java.lang.reflect.Field f = (java.lang.reflect.Field)e.nextElement();
          Method g = this.pobject.getClass().getMethod(this.buildGetMethodName(f.getName()), null);
          Method s = this.getClass().getMethod(this.buildSetMethodName(f.getName()), new Class[] {Class.forName("java.lang.String")});

//          System.out.println(g.getName());
//          System.out.println(this.pobject);
          // recuperando o valor do PObject
//          while (poSuperClasse.getName().indexOf("PObject") < 0) {
            Object o = g.invoke(this.pobject, null);
//            System.out.println(g.getName()+" - "+o);
            if (o != null){
              if (f.getType() == java.sql.Timestamp.class){
                o = br.com.hwork.text.Formatter.getDateTime(o);
              }
              // setando o valor do Bean
              s.invoke(this, new Object[] {o.toString()});
            }
//          }
        }
      }
    } finally {
      this.pobject = (PObject) this.pobjects.elementAt(0);
    }
  }

  private String getClassName(String s){return s.substring(s.lastIndexOf(".")+1);}

  /**
  * Recupera a instância do objeto persistente padrão para uso neste bean.
  */
  protected abstract PObject getPObjectInstance();

  /**
  * Copia os valores do bean para o objeto persistente padrão.
  */
  protected void beanToObject()
  throws Exception{
    this.pobjects.setElementAt(this.pobject,0);
    Iterator iPobjects = this.pobjects.iterator();
    try {
      while(iPobjects.hasNext()) {
        this.pobject = (PObject) iPobjects.next();
        this.defineFields();
        Enumeration e = this.commonFields.elements();
        while (e.hasMoreElements()){
          java.lang.reflect.Field f = (java.lang.reflect.Field)e.nextElement();
          Method g = this.getClass().getMethod(this.buildGetMethodName(f.getName()), null);
          Method s = this.pobject.getClass().getMethod(this.buildSetMethodName(f.getName()), new Class[] {f.getType()});

          // recuperando o valor do Bean
          String o = (String)g.invoke(this, null);
          if ((o != null) && (!o.equals(""))){
            // setando o valor do PObject
            Object value = null;
            if (f.getType() == java.sql.Timestamp.class){
                long dataLong = br.com.hwork.text.Formatter.getDateTimeLong(o);
                value = new java.sql.Timestamp(dataLong);
            }
            else{
			//System.out.println("f.getName()::"+f.getName());
              Constructor constructor = f.getType().getConstructor(new Class[]{java.lang.String.class});
              value = constructor.newInstance(new Object[] {o});
            }
            s.invoke(this.pobject, new Object[] {value});
          }
        }
      }
    } finally {
      this.pobject = (PObject) this.pobjects.elementAt(0);
    }
  }

  /**
  * Copy the last Period
  */
  protected void copyLastPeriod() throws Exception {}

  /*-------------------------------------------------------------------------
                      Metodos de suporte
  -------------------------------------------------------------------------*/
  /**
  * Trata o evento antes da postagem de dados.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforePost() throws Exception {}
  /**
  * Trata o evento antes da inserção de dados.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeInsert() throws Exception {}
  /**
  * Trata o evento antes da alteração de dados.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeUpdate() throws Exception {}
  /**
  * Trata o evento antes da deleção de dados.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeDelete() throws Exception {}
  /**
  * Trata o evento antes da seleção do objeto negocial padrão.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeSelectObject() throws Exception {}
  /**
  * Trata o evento antes da seleção da lista de objetos.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeSelectList() throws Exception {}
  /**
  * Trata o evento antes do cancelamento da operação.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeRecover() throws Exception {}
  /**
  * Trata o evento antes da rolagem de dados no grid.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeScroll() throws Exception {}
  /**
  * Trata o evento antes do tratamento dos erros encontrados em qualquer operação.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void beforeError(Exception ex){}
  /**
  * Trata o evento de formatção dos dados do bean.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void format() throws Exception {}
  /**
  * Trata o evento de desformatação dos dados do bean.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void unformat() throws Exception {}
  protected void doFormat() throws Exception {if(!this.isFormated) format();this.isFormated = true;}
  protected void doUnformat() throws Exception {if(this.isFormated) unformat();this.isFormated = false;}

  /**
  * Insere o objeto persistente padrão no banco de dados.<br>
  * Só deve ser subscrito nas subclasses caso se deseje alterar o processo
  * de inserção, como no caso de uso de controladores.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   pobject.insert();
  */
  protected void insert() throws Exception {this.pobject.insert();}
  /**
  * Altera o objeto persistente padrão no banco de dados.<br>
  * Só deve ser subscrito nas subclasses caso se deseje alterar o processo
  * de alteração, como no caso de uso de controladores.
  * Desencadeia as seguintes operações (nesta ordem)
  *   pobject.update();
  */
  protected void update() throws Exception {this.pobject.update();}
  /**
  * Deleta o objeto persistente padrão no banco de dados.<br>
  * Só deve ser subscrito nas subclasses caso se deseje alterar o processo
  * de deleção, como no caso de uso de controladores.
  * Desencadeia as seguintes operações (nesta ordem)
  *   pobject.delete();
  */
  protected void delete() throws Exception {
    Exception exception = null;
      try{
        this.pobject.delete();
      }
      catch(Exception ex){exception = ex;}
    if (exception != null) throw exception;
  }
  /**
  * Recupera um objeto com exclusão logica.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void recover() throws Exception {
    Exception exception = null;
      try{
        this.pobject.recover(null);
      }
      catch(Exception ex){exception = ex;}
    if (exception != null) throw exception;
  }

  /**
  * Seleciona o objeto negocial padrão do bean.<br>
  * <b>DEVE</b> ser subscrito em qualquer bean.
  * @param String[] s valores dos campos que compõem o ID (ou chave primária) do objeto.
  */
  protected abstract void selectObject(String[] s) throws Exception;

  public void selectObject() throws Exception{
    try {
      this.doFormat();
      this.beforeSelectObject();
      this.selectObject(this.getIDValues(this.objIds[0]));
      if (this.pobject!=null){
        this.objectToBean();
        this.afterSelectObject();
        this.doUnformat();
      }else{
        this.dbAction = ACTION_SELECT_LIST;
        execute();
        message = "O objeto selecionado foi excluido por outro usuario!";
      }
    } catch (Exception ex){
      this.dbAction = ACTION_SELECT_LIST;
      execute();
      message = "Houve um erro de perca do objIds no form update.jsp/displayList.jsp.";
      this.setBeanForm(this.getFormDisplayList());
    }
  }

  /**
  * Incrementa o cursor de navegação de lista para a próxima página.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   se não é último bloco -> blockNumber++;
  */
  protected void next() throws Exception{
    this.dbAction = ACTION_SELECT_LIST;
    execute();
    if (!isLastBlock()){this.blockNumber++;}
  }
  /**
  * Decrementa o cursor de navegação de lista para a página anterior.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   se não é primeiro bloco -> blockNumber--;
  */
  protected void prior() throws Exception {
    this.dbAction = ACTION_SELECT_LIST;
    execute();
    if (!isFirstBlock()) {this.blockNumber--;}
  }
  /**
  * Seleciona a lista de objetos negociais do bean.<br>
  * <b>DEVE</b> ser subscrito em qualquer bean.
  */
  protected void selectList() throws Exception{}
  /**
  * Trata as exceções que podem ocorrer durante o método execute().<br>
  * Pode ser subscrito nas subclasses, caso se deseje mudar o texto das mensagens.<br>
  * O texto das mensagens é armazenado no atributo message;
  */
  protected void execError(Exception ex){
    String msg = ex.getMessage();
    msg = msg.replace('\n', '.');
    this.message = "Error: " + ex.getMessage();
  }

  /**
  * Trata o evento depois da postagem de dados.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void afterPost() throws Exception {
  }

  /**
  * Trata o evento depois da inserção de dados.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   message = MSG_INSERT_OK;
  */
  protected void afterInsert() throws Exception {
    this.dbAction = ACTION_SELECT_LIST;
    this.execute();
    this.message = MSG_INSERT_OK;
  }

  /**
  * Trata o evento depois da alteração de dados.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   message = MSG_UPDATE_OK;
  */
  protected void afterUpdate() throws Exception {
    this.dbAction = ACTION_SELECT_LIST;
    this.execute();
    this.message = MSG_UPDATE_OK;
  }

  /**
  * Trata o evento depois da deleção de dados.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   dbAction = ACTION_SELECT_LIST;
  *   execute();
  *   message = MSG_DELETE_OK;
  *  //se esta no ultimo bloco e nao possui nada a mostrar entao
  *  // retorna para bloco anterior
  *  if ((this.blockNumber * this.blockSize)>=this.list.size()){
  *    this.dbAction = ACTION_PRIOR;
  *    this.execute();
  */
  protected void afterDelete() throws Exception {
    this.dbAction = ACTION_SELECT_LIST;
    this.execute();
    this.message = MSG_DELETE_OK;
    //se esta no ultimo bloco e nao possui nada a mostrar entao
    // retorna para bloco anterior
    if ((this.blockNumber * this.blockSize)>=this.list.size()){
      this.dbAction = ACTION_PRIOR;
      this.execute();
    }
  }

  /**
  * Trata o evento depois da seleção do objeto negocial padrão.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void afterSelectObject() throws Exception {
    this.setBeanForm(this.getFormUpdate());
  }

  /**
  * Trata o evento depois da seleção da lista de objetos negociais.<br>
  * Desencadeia as seguintes operações (nesta ordem)
  *   setBeanForm(getFormDisplayList());
  */
  protected void afterSelectList() throws Exception {
    this.setBeanForm(this.getFormDisplayList());
  }

  /**
  * Trata o evento depois do cancelamento da operação.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void afterRecover() throws Exception {
    this.dbAction = ACTION_SELECT_LIST;
    this.execute();
//    this.message = MSG_RECOVER_OK;
  }

  /**
  * Trata o evento depois do "rolamento" de página da lista de objetos negociais.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void afterScroll() throws Exception {}

  /**
  * Trata o evento depois da detecção de erros na execução de qualquer tarefa.<br>
  * Não implementa nenhuma característica.<br>
  * Pode ser subscrito nas subclasses.
  */
  protected void afterError(Exception ex){}

  /**
  * Retorna o formulário padrão de inserção: FORM_INSERT
  */
  protected String getFormInsert(){return FORM_INSERT;}

  /**
  * Retorna o formulário padrão de alteração: FORM_UPDATE
  */
  protected String getFormUpdate(){return FORM_UPDATE;}

  /**
  * Retorna o formulário padrão de deleção: FORM_DELETE
  */
  protected String getFormDelete(){return FORM_DELETE;}

  /**
  * Retorna o formulário padrão de lista de objetos: FORM_DISPLAY_LIST
  */
  protected String getFormDisplayList(){return FORM_DISPLAY_LIST;}

  /**
  * Retorna o formulário padrão de pesquisa de objetos: FORM_SEARCH
  */
  protected String getFormSearch(){return FORM_SEARCH;}

  /**
  * Retorna o formulário padrão de erros: FORM_ERROR
  */
  protected String getFormError(){return FORM_ERROR;}

    /**
  * Executa a operação corrente do bean (dbAction).
  */
  public void execute() {

    try{
       this.doFormat();
       if (this.dbAction == ACTION_NONE){this.dbAction = ACTION_SELECT_LIST;}
        switch (this.dbAction){
          case ACTION_NONE:{break;}
          case ACTION_INSERT:{this.beforeInsert();beforePost();this.beanToObject();this.insert();afterPost();this.afterInsert();break;}
          case ACTION_UPDATE:{this.beforeUpdate();beforePost();this.beanToObject();this.update();afterPost();this.afterUpdate();break;}
          case ACTION_UPDATE_EDIT:{this.beforeUpdate();beforePost();this.beanToObject();this.update();this.afterSelectObject();break;}
		  case ACTION_DELETE:{
			for (int i=1; i<this.objIds.length;i++) {
			  this.beforeDelete();
			  this.beanToObject();
			  String[] idValues = this.getIDValues(this.objIds[i]);
			  this.selectObject(idValues);
			  this.delete();
			  this.afterDelete();
			}
			break;
		  }
          case ACTION_SELECT_OBJECT:{
            this.beforeSelectObject();
            this.selectObject(this.getIDValues(this.objIds[0]));
            if (this.pobject==null){
              this.dbAction = ACTION_SELECT_LIST;
              this.execute();
              this.message="Error: Objeto Inexistente ou Excluido!";
              break;
						}
            this.objectToBean();
            this.afterSelectObject();
            break;}
          case ACTION_SELECT_LIST:{
            if (!this.errorSelectList){
              this.beforeSelectList();
              this.selectList();
              this.afterSelectList();
              break;
            }
            else{this.list = new Vector();}
          }
          case ACTION_NEXT:{this.beforeScroll();this.next();this.afterScroll();break;}
          case ACTION_PRIOR:{this.beforeScroll();this.prior();this.afterScroll();break;}
          case ACTION_FORM_INSERT:{
            this.setBeanForm(this.getFormInsert());
            break;
          }
          case ACTION_FORM_UPDATE:{
            this.dbAction = ACTION_SELECT_OBJECT;
            this.execute();
            this.setBeanForm(this.getFormUpdate());
            break;
          }
          case ACTION_FORM_DELETE:{
            this.dbAction = ACTION_SELECT_OBJECT;
            this.execute();
            this.setBeanForm(this.getFormDelete());
            break;
          }
          case ACTION_FORM_DISPLAY_LIST:{
            this.setBeanForm(this.getFormDisplayList());
            break;
          }
          case ACTION_FORM_ERROR:{
            this.setBeanForm(this.getFormError());
            break;
          }
          case ACTION_ERROR:{break;}
        }
    }
    catch (Exception ex){
      ex.printStackTrace();
//      ex.printStackTrace(this.out);
      this.beforeError(ex);this.execError(ex);this.afterError(ex);
      if(!erro) {
        erro = true;
        switch (this.dbAction){
          case ACTION_SELECT_LIST:{this.errorSelectList = true;this.setBeanForm("../common/errorpge.jsp");break;}
          case ACTION_SELECT_OBJECT:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          case ACTION_INSERT:{this.dbAction = ACTION_FORM_INSERT;this.execute();break;}
          case ACTION_UPDATE:{this.dbAction = ACTION_FORM_UPDATE;this.execute();break;}
          //case ACTION_UPDATE:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          case ACTION_DELETE:{this.dbAction = ACTION_SELECT_LIST;this.execute();break;}
          case ACTION_NEXT:{this.dbAction = ACTION_FORM_DISPLAY_LIST;this.execute();break;}
          case ACTION_PRIOR:{this.dbAction = ACTION_FORM_DISPLAY_LIST;this.execute();break;}
        }
      }
    }
    try {
      this.doUnformat();
    } catch(Exception ex) {
      ex.printStackTrace();
//      ex.printStackTrace(this.out);
      this.beforeError(ex);this.execError(ex);this.afterError(ex);
    }
  }

  /**
  * Quebra os valores de identificação de um objeto dado um id composto.
  * @param id Identificador composto de um objeto
  * @return Porções do identificador. Cada porção ocupa uma posição no vetor de Strings.
  */
  public String[] getIDValues(String id){
    String[] idValues = null;
    java.util.StringTokenizer st = new java.util.StringTokenizer(id, HTMLComponent.VALUE_SEPARATOR);
    if (st.countTokens()>0){
      idValues = new String[st.countTokens()];
      for (int i=0; i<idValues.length; i++){
        idValues[i] = st.nextToken();
      }
    }
    return idValues;
  }

  public String getObjIds(String[] ids){
    String objIds = "";
    for (int i=0; i<ids.length; i++){
     try{
      Method g = this.getClass().getMethod(this.buildGetMethodName(ids[i]), null);
      Object o = g.invoke(this, null);
      if (i>0) objIds += HTMLComponent.VALUE_SEPARATOR;
      objIds += (String)o;
     }
     catch(Exception ex){objIds = "";}
    }
    return objIds;
  }

  protected abstract String getURLInicio();

  public static String replaceAll(String str, String s1, String s2) throws Exception{
	  int pos=0;
	  int oldPos;
	  int nextPos = 0;
	  String newStr="";
	  String oldStr=str;
	  int check = 0;
	  while (pos>=0){
		  pos = oldStr.indexOf(s1);
		  if (pos>-1){
			newStr = oldStr.substring(0,pos)+s2+oldStr.substring(pos+s1.length());
			oldStr = newStr;
		  }
		  check = check + 1;
		  if (check>1000)
		  	throw new Exception("This method maybe stay in loop, and because this is abort!");
	  }
	  return oldStr;
  }

  public static String replace(String str, String s1, String s2){
	  int pos=0;
	  int oldPos;
	  int nextPos = 0;
	  String newStr="";
	  String oldStr=str;
	  pos = oldStr.indexOf(s1);
	  if (pos>-1){
		newStr = oldStr.substring(0,pos)+s2+oldStr.substring(pos+s1.length());
		oldStr = newStr;
	  }
	  return oldStr;
  }

  public static String getTabSheet(String tabSheetOn,
  								   String[] tabSheetOff,
  								   String[] tabSheetOffAction,
  								   String[] tabSheetOffSize){
  	String html = "";
  	int tabs = tabSheetOff.length;
	html += "<table width=\"100%\" class=\"tabGuia\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\r";
	html += "<tr>\r";
	for (int i=0;i<tabs;i++){
		if (tabSheetOff[i].equals(tabSheetOn)){
			html += "  <td class=\"tdEsqOn\">&nbsp;</td>\r";
			html += "  <td width=\""+tabSheetOffSize[i]+"\" nowrap class=\"tdGuiaOn\">"+tabSheetOn+"</td>\r";
			html += "  <td class=\"tdDirOn\">&nbsp;</td>\r";
		}else{
			html += "  <td class=\"tdEsqOff\">&nbsp;</td>\r";
			html += "  <td width=\""+tabSheetOffSize[i]+"\" nowrap class=\"tdGuiaOff\"><input type=\"button\" value=\""+tabSheetOff[i]+"\" onClick=\""+tabSheetOffAction[i]+"\" style=\"{width: "+tabSheetOffSize[i]+"px;}\" name=\"btnAnterior\" id=\"btnGuia\"></td>\r";
			html += "  <td class=\"tdDirOff\">&nbsp;</td>\r";
		} 
	}
	html += "  <td width=\"*\">&nbsp;</td>\r";
	html += "</tr>\r";
	html += "</table>\r";
	return html;
  }

  protected Connection getConnection(String contextType, String jndiName) throws Exception{
	  Connection conn = null;
	  DataSource ds = null;
	  contextType = PropertiesManager.getString("application.contextType");
	  ds = ServiceLocator.getInstance(contextType).getDataSource(jndiName);
	  conn = ds.getConnection();
	  return conn;
  }

	public String getInsertStmp(PObject pobject) {
		try {
			MetaCol[] metaCols = pobject.getDataMetaCols();
			Method findAll = pobject.getClass().getMethod("findAll", null); 
			Vector objs = (Vector)findAll.invoke(pobject, null);
			String insertStmt = "INSERT INTO "+metaCols[0].getTableName()+" (";
			for (int i=0;i<metaCols.length;i++){
				insertStmt += metaCols[i].getColName();
				if ((i+1)==metaCols.length)
					insertStmt += ")";
				else
					insertStmt += ",";	  		
			}
			insertStmt += " VALUES (";
			String SQL = "";
			for (int j=0;j<objs.size();j++){
				PObject obj = (PObject)objs.elementAt(j);
				String stmt = "";
				for (int i=0;i<metaCols.length;i++){
					Method m = metaCols[i].getGetMethod(); 
					Object o = m.invoke(obj, null);
					if (o!=null){
						if (m.getReturnType()==java.lang.String.class || m.getReturnType()==java.sql.Timestamp.class)
							stmt += "'"+o.toString()+"'";
						else
							stmt += o.toString();
					}else
						stmt += "null";
					if ((i+1)==metaCols.length)
						SQL += insertStmt + stmt + ");\n";
					else
						stmt += ",";	  		
				}
			}
			return SQL;
		}catch (Exception e){
			e.printStackTrace();
			return "";
		}
		
	}
}
