package reports;

public class QueryMetaCols {
	private String fieldName;
	private String fieldComment;
	private String fieldType;

	public void setFieldName(String fieldName) {this.fieldName = fieldName;}
	public void setFieldComment(String fieldComment) {this.fieldComment = fieldComment;}
	public void setFieldType(String fieldType) {this.fieldType = fieldType;}
	public String getFieldName() {return this.fieldName;}
	public String getFieldComment() {return this.fieldComment;}
	public String getFieldType() {return this.fieldType;}

	public QueryMetaCols() {
	}
}

