		//queryStr = queryStr.replace("%22" , "\"");
    	StringBuffer query_text = new StringBuffer(queryStr.replace( "%20" , " ") );
    	
    	int posn = query_text.indexOf(" ");
