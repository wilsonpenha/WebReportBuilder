// Copyright 2002 Ananta Risal (arisal@bright-ideas.com)

currentCol = 0;
previousCol = -1;
var counter=0;
var dateCol = 6;		
	
function CompareAlpha(a, b){
	if (a[currentCol] < b[currentCol]) { return -1; }
	if (a[currentCol] > b[currentCol]) { return 1; }
	window.status='sn:' + counter++ ;
	return 0;
}

function CompareAlphaAscending(a, b) {
	if (a[currentCol] < b[currentCol]) { return -1; }
	if (a[currentCol] > b[currentCol]) { return 1; }
	
	if (a[currentCol]==b[currentCol]){ 	
		datA = new Date(a[dateCol]);
		datB = new Date(b[dateCol]);	
		//alert("DateA: " + datA + " DateB:" + datB);		
		if (datA < datB){
			 return 1; 
	  }
		else{
				 if (datA > datB){
				   return -1; 
				 }
				 else{
				 		return 0; 
				 }
	  }
	}
	window.status='sn:' + counter++ ;
	return 0;
}

function CompareAlphaDescending(a, b) {
	if (a[currentCol] < b[currentCol]) { return 1; }
	if (a[currentCol] > b[currentCol]) { return -1; }
	
	if (a[currentCol]==b[currentCol]){ 	
		datA = new Date(a[dateCol]);
		datB = new Date(b[dateCol]);	
		//alert("DateA: " + datA + " DateB:" + datB);		
		if (datA < datB){
			 return 1; 
	  }
		else{
				 if (datA > datB){
				   return -1; 
				 }
				 else{
				 		return 0; 
				 }
	  }
	}
	window.status='sn:' + counter++ ;
	return 0;
}

function CompareAlphaIgnore(a, b) {
	strA = a[currentCol].toLowerCase();
	strB = b[currentCol].toLowerCase();
	if (strA < strB) { return -1; }
	else {
		if (strA > strB) { return 1; }
		else { return 0; }
	}
}

function CompareAlphaIgnoreArray(a, b) {
 //This function Uses the Array Sorting NOT the Table Column sorting
	strA = a.toLowerCase();
	strB = b.toLowerCase();
	if (strA < strB) { return -1; }
	else {
		if (strA > strB) { return 1; }
		else { return 0; }
	}
}

function CompareAlphaIgnoreDescending(a, b) {
	strA = a[currentCol].toLowerCase();
	strB = b[currentCol].toLowerCase();
	//alert("A= "+ strA +"\nB="+ strB)
	if (strA > strB) { return -1; }
	else {
		if (strA < strB) { return 1; }
		else { return 0; }
	}
}

function CompareAlphaIgnoreAscending(a, b) {
	strA = a[currentCol].toLowerCase();
	strB = b[currentCol].toLowerCase();
	//alert("A= "+ strA +"\nB="+ strB)
	if (strA < strB) { return -1; }
	else {
		if (strA > strB) { return 1; }
		else { return 0; }
	}
}

function CompareDate(a, b) {
	// this one works with date formats conforming to Javascript specifications, e.g. m/d/yyyy
	datA = new Date(a[currentCol]);
	datB = new Date(b[currentCol]);
	if (datA < datB) { return -1; }
	else {
		if (datA > datB) { return 1; }
		else { return 0; }
	}
}

function CompareDateAscending(a, b) {
	// this one works with date formats conforming to Javascript specifications, e.g. m/d/yyyy
	if(isDate(a[currentCol])){
		datA = new Date(a[currentCol]);
	}else{
		datA = new Date("01/01/2200");
	}	
	if(isDate(b[currentCol])){
		datB = new Date(b[currentCol]);
	}else{
		datB = new Date("01/01/2200");
	}
	if (datA < datB) { return -1; }
	else {
		if (datA > datB) { return 1; }
		else { return 0; }
	}
}
function CompareDateDescending(a, b) {
	// this one works with date formats conforming to Javascript specifications, e.g. m/d/yyyy
	if(isDate(a[currentCol])){
		datA = new Date(a[currentCol]);
	}else{
			datA = new Date("01/01/1000");
	}	
	if(isDate(b[currentCol])){
		datB = new Date(b[currentCol]);
	}else{
		datB = new Date("01/01/1000");
	}		
	if (datA < datB) { return 1; }
	else {
		if (datA > datB) { return -1; }
		else { return 0; }
	}
}

function isDate(dateStr) {
    var datePat = /^(\d{1,2})(\/|-)(\d{1,2})(\/|-)(\d{4}|\d{2})$/;
    var matchArray = dateStr.match(datePat); 
		//alert(datePat + "MatchArray="+matchArray[1]);
    if (matchArray == null) {
        //alert("Please enter date as either mm/dd/yyyy or mm-dd-yyyy.");
        return false;
    }
    month = matchArray[1]; // parse date into variables
    day = matchArray[3];
    year = matchArray[5];

    if (month < 1 || month > 12) { // check month range
        //alert("Month must be between 1 and 12.");
        return false;
    }

    if (day < 1 || day > 31) {
        //alert("Day must be between 1 and 31.");
        return false;
    }

    if ((month==4 || month==6 || month==9 || month==11) && day==31) {
        //alert("Month "+month+" doesn't have 31 days!")
        return false;
    }

    if (month == 2) { // check for february 29th
        var isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
        if (day > 29 || (day==29 && !isleap)) {
            //alert("February " + year + " doesn't have " + day + " days!");
            return false;
        }
    }
    return true; // date is valid
}
function CompareDateEuro(a, b) {
	// this one works with european date formats, e.g. d.m.yyyy
	strA = a[currentCol].split(".");
	strB = b[currentCol].split(".")
	datA = new Date(strA[2], strA[1], strA[0]);
	datB = new Date(strB[2], strB[1], strB[0]);
	if (datA < datB) { return -1; }
	else {
		if (datA > datB) { return 1; }
		else { return 0; }
	}
}

function CompareNumeric(a, b) {
	//window.alert ("CompareNumeric");
	numA = a[currentCol]
	numB = b[currentCol]
	if (isNaN(numA)) { return 0;}
	else {
		if (isNaN(numB)) { return 0; }
		else { return numA - numB; }
	}
}

function CompareDateEuroAscending(a, b) {
	// this one works with european date formats, e.g. d.m.yyyy
	//alert("ASC Col=" + currentCol +  " a=" + a[currentCol] + " b=" + b[currentCol]);
	strA = a[currentCol].split("/");
	strB = b[currentCol].split("/")

	datA = new Date(strA[2], strA[1]-1, strA[0]);
	if ( isNaN(datA) ) {
       datA = new Date( 2100 , 1 , 1 ) ;      
	}
	datB = new Date(strB[2], strB[1]-1, strB[0]);
	if ( isNaN(datB) ) {
       datB = new Date( 2100 , 1 , 1 ) ;      
	}
	//alert(" ASC datA:" + datA + "datB:" + datB);
	if (datA < datB) { return -1; }
	else {
		if (datA > datB) { return 1; }
		else { return 0; }
	}
}

function CompareDateEuroDescending(a, b) {
	// this one works with european date formats, e.g. d.m.yyyy
	//alert("DES COl=" + currentCol + "a=" + a[currentCol] + " b=" + b[currentCol]);
	strA = a[currentCol].split("/");
	strB = b[currentCol].split("/")
	datA = new Date(strA[2], strA[1], strA[0]);
	if ( isNaN(datA) ) {
       datA = new Date( 1900 , 1 , 1 ) ;      
	}
	datB = new Date(strB[2], strB[1], strB[0]);
	if ( isNaN(datB) ) {
       datB = new Date( 1900 , 1 , 1 ) ;      
	}
	//alert(" DESC datA:" + datA + "datB:" + datB);
	if (datA < datB) { return 1; }
	else {
		if (datA > datB) { return -1; }
		else { return 0; }
	}
}

function CompareNumericAscending(a, b) {
	//window.alert ("CompareNumeric");
  numA = parseInt(a[currentCol] );
	numB = parseInt(b[currentCol] );
	if (isNaN(numA)){numA = -1;}
	if (isNaN(numB)){numB = -1;}
	if(numA < numB){return -1;}
	else{
			 if (numA > numB){return 1;}
			 else {return 0;}
	}	
}
function CompareNumericDescending(a, b) {
	//window.alert ("CompareNumeric");
  numA = parseInt(a[currentCol] );
	numB = parseInt(b[currentCol] );
	if (isNaN(numA)){numA = -1;}
	if (isNaN(numB)){numB = -1;}
	if(numA > numB){return -1;}
	else{
			 if (numA < numB){return 1;}
			 else {return 0;}
	}	
}

function TableSort(myTable, myCol, myType, sRow, cf) {

	//document.body.style.cursor = "wait";
	if (!document.all.WaitMessageId){
	waitWindow()
	}
   var startRow=1;
   //document.all.ResultDiv.style.cursor="wait";
	//alert("Function=TableSort"+" myTable=" + myTable + " myCol=" + myCol +" myType=" + myType + " sRow=" +sRow);
	if(sRow=='undefined'){
			startRow=1;
		  //alert("undefined:"+startRow);
	}else{
				if(sRow==0){
				 startRow=0;
				 //alert("value of row=0:" + startRow);
				}
				if(sRow==1){
					startRow=1;
				 //alert("value of row=1:" + startRow);
				}
	}
	//alert("sRow="+sRow + " startRow="+startRow)
	
  counter=0;
	var mySource = document.all[myTable];
	var myRows = mySource.rows.length;
	
	var myCols = mySource.rows[0].cells.length;
	currentCol = myCol;
	myArray = new Array(myRows);		
	for (i=startRow; i < myRows; i++) {
		myArray[i] = new Array(myCols);
		for (j=0; j < myCols; j++) {
					myArray[i][j] = document.all(myTable).rows(i).cells(j).innerHTML;
					//window.status = "Records Read=" + i +" of " + myRows;
					//alert("i=" + i + " j:" + j + " value:" + myArray[i][j]);
		}
	}  	
	switch (myType){
			case "a":
				myArray.sort(CompareAlpha);
				break;
			case "aa":
				//window.status = "Sorting Started..!" ;
				myArray.sort(CompareAlphaIgnoreAscending);
			    //window.status = "Sorting Completed..!" ;
				break;
			case "ad":
				myArray.sort(CompareAlphaIgnoreDescending);
				break;				
			case "ai":
				myArray.sort(CompareAlphaIgnore);
				break;
			case "d":
				myArray.sort(CompareDate);
				break;
			case "da":
				myArray.sort(CompareDateAscending);
				break;
			case "dd":
				myArray.sort(CompareDateDescending);
				break;
			case "de":
				myArray.sort(CompareDateEuro);
				break;
			case "dea":
				myArray.sort(CompareDateEuroAscending);
				break;
			case "ded":
				myArray.sort(CompareDateEuroDescending);
				break;
			case "n":
				myArray.sort(CompareNumeric);
				break;
			case "na":
				myArray.sort(CompareNumericAscending);
				break;
			case "nd":
				myArray.sort(CompareNumericDescending);
				break;
			default:
				myArray.sort();
		}
	for (i=startRow; i < myRows; i++) {
		for (j=0; j < myCols; j++){
		  if (startRow==1){
				 mySource.rows(i).cells(j).innerHTML = myArray[i-1][j];
			}else{
				 mySource.rows(i).cells(j).innerHTML = myArray[i][j];
			}	 
			//window.status = "Records Write =" + i +" of " + myRows ;
		}
	}
	previousCol = myCol; // remember the current sort column for the next pass
	if (cf=='view'){
	
		if (!document.all.WaitMessageId){
	waitWindow()
	}
		document.all.WaitMessageId.style.display = "none" ;
	}
	return 0;
}

function TableSortWait(TableName, StartRow, SortType, IncludeFirstRow){
	if (!document.all.WaitMessageId){
	waitWindow()
	}
document.all.WaitMessageId.style.display = "block" ;
setTimeout("TableSort(\"" + TableName + "\",\"" + StartRow + "\",\"" + SortType + "\",\"" + IncludeFirstRow + "\",'view')", '500');
}

function waitWindow() {

str= ' <DIV class="Wait" id="WaitMessageId" style="cursor:wait; display:none;"> '
str+='<DIV class="waitTable" style="position:absolute;top:200px;left:400px">'
str+='<TABLE BGCOLOR="#000000" BORDER="1" BORDERCOLOR="#000000" CELLPADDING="0" CELLSPACING="0" HEIGHT="50" WIDTH="150">'
str+='<TR>'
str+='<TD WIDTH="100%" HEIGHT="100%" BGCOLOR="#CCCCCC" ALIGN="CENTER" VALIGN="MIDDLE">'
str+='<FONT FACE="Verdana" SIZE="1" COLOR="#000066"><B>&nbsp;&nbsp;&nbsp;&nbsp;Please wait ! Sorting..</B></FONT><hr> &nbsp;&nbsp;&nbsp;&nbsp;<IMG SRC="common/images/wait.gif" BORDER="1" HEIGHT="10" WIDTH="100"><BR>'
str+='<br>'
str+='</TD>'
str+='</TR>'
str+='</TABLE>'
str+='</DIV>'
str+='</DIV>'
document.body.insertAdjacentHTML("beforeEnd",str)
}