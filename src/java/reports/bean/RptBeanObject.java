package reports.bean;

import java.util.Vector;
import br.com.hwork.persistencia.PObject;

public abstract class RptBeanObject extends BeanObject {

  public static String MSG_MESMO_LOGIN = "INSER��O N�O OCORRIDA. Existe um usu�rio j� cadastrado com esse login !";
  public static String MSG_MESMO_CODIGO = "INSER��O N�O OCORRIDA. Existe um parceiro j� cadastrado com esse c�digo !";

  protected String getURLInicio()
  {
  	return "/pge/jsp/main.jsp";
  }

}


























