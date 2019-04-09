/************************************************************/
//Titulo.....: Projeto
//Empresa....: hwork
//Versao.....:
//Alterado em:

package reports;

import java.util.*;
import br.com.hwork.persistencia.*;

/************************************************************/

public class InsTransacoesSistema extends PObject{

  /*-------------------------------------------------------------------------
                      Atributos Negociais
  -------------------------------------------------------------------------*/
  private java.sql.Timestamp trsDhTransacao;
  private java.lang.String trsEdPagina;
  private java.lang.String trsInAjudaVisivel;
  private java.lang.String trsInIntranetVisivel;
  private java.lang.String trsInPaginaInicial;
  private java.lang.String trsInSituacao;
  private java.lang.String trsNoAjuda;
  private java.lang.String trsNoPagina;
  private java.lang.Double trsNuTelaAnterior;
  private java.lang.Double trsNuTransacaoSistema;
  private java.lang.Double trsNuTransacaoSistemaPai;
  private java.lang.String trsTmMenu;
  private java.lang.String trsTmSelecao;
  private java.lang.String trsTxDescTransacao;
  private java.lang.String usuCoObjeto;
  private static Vector vInsTransacoesSistema;
  private static Hashtable hInsTransacoesSistemaID;     

  static {
  	//new Double("10").  
    vInsTransacoesSistema = new Vector();
    java.sql.Timestamp timestamp = new java.sql.Timestamp(new java.util.Date().getTime());
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("1"), Double.valueOf("1"), "Report Designer"                   ,  "A",  "1",  timestamp, "Report Designer"                  ,"S","S","","/jsp/beanReport/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("2"), Double.valueOf("2"), "Variables"                   ,  "A",  "1",  timestamp, "Variables"                  ,"S","S","","/jsp/beanVariables/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("3"), Double.valueOf("3"), "Parameters"                   ,  "A",  "1",  timestamp, "Parameters"                  ,"S","S","","/jsp/beanParameters/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("4"), Double.valueOf("4"), "Query Fields"                   ,  "A",  "1",  timestamp, "Query Fields"                  ,"S","S","","/jsp/beanFields/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("5"), Double.valueOf("5"), "Detail Columns"                   ,  "A",  "1",  timestamp, "Detail Columns"                  ,"S","S","","/jsp/beanColumns/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("6"), Double.valueOf("6"), "Title Columns"                   ,  "A",  "1",  timestamp, "Title Columns"                  ,"S","S","","/jsp/beanReportColumns/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("7"), Double.valueOf("7"), "Page Columns"                   ,  "A",  "1",  timestamp, "Page Columns"                  ,"S","S","","/jsp/beanPageColumns/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("8"), Double.valueOf("8"), "Summary Columns"                   ,  "A",  "1",  timestamp, "Summary Columns"                  ,"S","S","","/jsp/beanSummaryColumns/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("9"), Double.valueOf("9"), "Report Groups"                   ,  "A",  "1",  timestamp, "Report Groups"                  ,"S","S","","/jsp/beanGroups/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("10"), Double.valueOf("10"), "Group Columns"                   ,  "A",  "1",  timestamp, "Group Columns"                  ,"S","S","","/jsp/beanGroupColumns/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("11"), Double.valueOf("11"), "Report Builder"                   ,  "A",  "1",  timestamp, "Report Builder"                  ,"S","S","","/jsp/beanMainReport/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("12"), Double.valueOf("12"), "Report Server"                   ,  "A",  "1",  timestamp, "Report Server"                  ,"S","S","","/jsp/beanReportServer/"                   ,Double.valueOf("1"),"","",""));
	vInsTransacoesSistema.add(new InsTransacoesSistema(Double.valueOf("13"), Double.valueOf("13"), "Database Table"                   ,  "A",  "1",  timestamp, "Database Table"                  ,"S","S","","/jsp/beanDatabaseTable/"                   ,Double.valueOf("1"),"","",""));
    hInsTransacoesSistemaID = new Hashtable();
    Iterator iInsTransacoesSistema;
    iInsTransacoesSistema = vInsTransacoesSistema.iterator();  
    while(iInsTransacoesSistema.hasNext()) {
      InsTransacoesSistema insTransacoesSistema = (InsTransacoesSistema) iInsTransacoesSistema.next();
      hInsTransacoesSistemaID.put(insTransacoesSistema.getTrsNuTransacaoSistema(), insTransacoesSistema);
    }
  }


  /*-------------------------------------------------------------------------
                      Construtores
  -------------------------------------------------------------------------*/
  public InsTransacoesSistema() {super();}
  private InsTransacoesSistema(
      Double trsNuTransacaoSistema,
      Double trsNuTransacaoSistemaPai,
      String trsTxDescTransacao,
      String trsInSituacao,
      String usuCoObjeto,
      java.sql.Timestamp trsDhTransacao,
      String trsNoPagina,
      String trsInPaginaInicial,
      String trsInAjudaVisivel,
      String trsInIntranetVisivel,
      String trsEdPagina,
      Double trsNuTelaAnterior,
      String trsNoAjuda,
      String trsTmSelecao,
      String trsTmMenu) {
    super();
    this.trsDhTransacao = trsDhTransacao;
    this.trsEdPagina = trsEdPagina;
    this.trsInAjudaVisivel = trsInAjudaVisivel;
    this.trsInIntranetVisivel = trsInIntranetVisivel;
    this.trsInPaginaInicial = trsInPaginaInicial;
    this.trsInSituacao = trsInSituacao;
    this.trsNoAjuda = trsNoAjuda;
    this.trsNoPagina = trsNoPagina;
    this.trsNuTelaAnterior = trsNuTelaAnterior;
    this.trsNuTransacaoSistema = trsNuTransacaoSistema;
    this.trsNuTransacaoSistemaPai = trsNuTransacaoSistemaPai;
    this.trsTmMenu = trsTmMenu;
    this.trsTmSelecao = trsTmSelecao;
    this.trsTxDescTransacao = trsTxDescTransacao;
    this.usuCoObjeto = usuCoObjeto;
  }

  /*-------------------------------------------------------------------------
                      Metodos de Acesso GET
  -------------------------------------------------------------------------*/
  public java.sql.Timestamp getTrsDhTransacao(){return this.trsDhTransacao;}
  public java.lang.String getDescricaoTrsDhTransacao(){return br.com.hwork.text.Formatter.getDate(this.getTrsDhTransacao());}
  public java.lang.String getTrsEdPagina(){return this.trsEdPagina;}
  public java.lang.String getTrsInAjudaVisivel(){return this.trsInAjudaVisivel;}
  public java.lang.String getTrsInIntranetVisivel(){return this.trsInIntranetVisivel;}
  public java.lang.String getTrsInPaginaInicial(){return this.trsInPaginaInicial;}
  public java.lang.String getTrsInSituacao(){return this.trsInSituacao;}
  public java.lang.String getTrsNoAjuda(){return this.trsNoAjuda;}
  public java.lang.String getTrsNoPagina(){return this.trsNoPagina;}
  public java.lang.Double getTrsNuTelaAnterior(){return this.trsNuTelaAnterior;}
  public java.lang.String getDescricaoTrsNuTelaAnterior(){return br.com.hwork.text.Formatter.getDoubleString(this.getTrsNuTelaAnterior());}
  public java.lang.Double getTrsNuTransacaoSistema(){return this.trsNuTransacaoSistema;}
  public java.lang.String getDescricaoTrsNuTransacaoSistema(){return br.com.hwork.text.Formatter.getDoubleString(this.getTrsNuTransacaoSistema());}
  public java.lang.Double getTrsNuTransacaoSistemaPai(){return this.trsNuTransacaoSistemaPai;}
  public java.lang.String getDescricaoTrsNuTransacaoSistemaPai(){return br.com.hwork.text.Formatter.getDoubleString(this.getTrsNuTransacaoSistemaPai());}
  public java.lang.String getTrsTmMenu(){return this.trsTmMenu;}
  public java.lang.String getTrsTmSelecao(){return this.trsTmSelecao;}
  public java.lang.String getTrsTxDescTransacao(){return this.trsTxDescTransacao;}
  public java.lang.String getUsuCoObjeto(){return this.usuCoObjeto;}

  /*-------------------------------------------------------------------------
                      Metodos de Acesso SET
  -------------------------------------------------------------------------*/
  public void setTrsDhTransacao(java.sql.Timestamp trsDhTransacao){this.trsDhTransacao = trsDhTransacao;}
  public void setTrsEdPagina(java.lang.String trsEdPagina){this.trsEdPagina = trsEdPagina;}
  public void setTrsInAjudaVisivel(java.lang.String trsInAjudaVisivel){this.trsInAjudaVisivel = trsInAjudaVisivel;}
  public void setTrsInIntranetVisivel(java.lang.String trsInIntranetVisivel){this.trsInIntranetVisivel = trsInIntranetVisivel;}
  public void setTrsInPaginaInicial(java.lang.String trsInPaginaInicial){this.trsInPaginaInicial = trsInPaginaInicial;}
  public void setTrsInSituacao(java.lang.String trsInSituacao){this.trsInSituacao = trsInSituacao;}
  public void setTrsNoAjuda(java.lang.String trsNoAjuda){this.trsNoAjuda = trsNoAjuda;}
  public void setTrsNoPagina(java.lang.String trsNoPagina){this.trsNoPagina = trsNoPagina;}
  public void setTrsNuTelaAnterior(java.lang.Double trsNuTelaAnterior){this.trsNuTelaAnterior = trsNuTelaAnterior;}
  public void setTrsNuTransacaoSistema(java.lang.Double trsNuTransacaoSistema){this.trsNuTransacaoSistema = trsNuTransacaoSistema;}
  public void setTrsNuTransacaoSistemaPai(java.lang.Double trsNuTransacaoSistemaPai){this.trsNuTransacaoSistemaPai = trsNuTransacaoSistemaPai;}
  public void setTrsTmMenu(java.lang.String trsTmMenu){this.trsTmMenu = trsTmMenu;}
  public void setTrsTmSelecao(java.lang.String trsTmSelecao){this.trsTmSelecao = trsTmSelecao;}
  public void setTrsTxDescTransacao(java.lang.String trsTxDescTransacao){this.trsTxDescTransacao = trsTxDescTransacao;}
  public void setUsuCoObjeto(java.lang.String usuCoObjeto){this.usuCoObjeto = usuCoObjeto;}


  /*-------------------------------------------------------------------------
                      Metodos de Navegacao
  -------------------------------------------------------------------------*/


  /*-------------------------------------------------------------------------
                      Metodos para Eventos
  -------------------------------------------------------------------------*/
  protected void beforePost(String transID)
  throws Exception{
  }

  /*-------------------------------------------------------------------------
                      Metodos Negociais de Sistema
  -------------------------------------------------------------------------*/
  /**
  * Recupera um objeto desta classe dado o seu identificador
  */
  public static InsTransacoesSistema recuperaPorID(
        java.lang.Double trsNuTransacaoSistema)
  throws Exception{
    return (InsTransacoesSistema) hInsTransacoesSistemaID.get(trsNuTransacaoSistema);
  }

  /**
  * Recupera todos os objetos desta classe
  */
  public static Vector recuperaTodos()
  throws Exception{
    return new Vector(vInsTransacoesSistema);
  }

  /*
   * Utilizado pelo módulo Falhas.
   */
  public static Vector recuperaPorTransacaoPaiDescricao(Double d, String s) throws Exception {
    InsTransacoesSistema its = new InsTransacoesSistema();
    its.trsNuTransacaoSistemaPai = d;
    its.trsTxDescTransacao = s;
    Vector v = new Vector();
    Iterator i = vInsTransacoesSistema.iterator();
    while(i.hasNext()) {
      InsTransacoesSistema currentIts = (InsTransacoesSistema)i.next();
      System.out.println("PAI1 => "+currentIts.trsNuTransacaoSistemaPai.doubleValue()+"<<");
      System.out.println("PAI2 => "+its.trsNuTransacaoSistemaPai.doubleValue()+"<<");
      System.out.println("DESC1 => "+currentIts.trsTxDescTransacao+"<<");
      System.out.println("DESC2 => "+its.trsTxDescTransacao+"<<");
      if((currentIts.trsNuTransacaoSistemaPai.doubleValue() == its.trsNuTransacaoSistemaPai.doubleValue()
          && (currentIts.trsTxDescTransacao.equals(its.trsTxDescTransacao)))) {
            v.add(currentIts);
            break;
      }
    }
    return v;
  }

}


/************************************************************/
// hwork, 2001
/************************************************************/
