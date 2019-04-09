package reports.bean;

import java.util.Vector;
import br.com.hwork.persistencia.PObject;

public abstract class RptBeanObject extends BeanObject {

  public static String MSG_MESMO_LOGIN = "INSERÇÃO NÃO OCORRIDA. Existe um usuário já cadastrado com esse login !";
  public static String MSG_MESMO_CODIGO = "INSERÇÃO NÃO OCORRIDA. Existe um parceiro já cadastrado com esse código !";

  protected String getURLInicio()
  {
  	return "/pge/jsp/main.jsp";
  }

}


























