<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="it" class="h-100" >
	 <head>

	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	 	
	   <title>Visualizza Elemento</title>
	   
	 </head>
	   <body class="d-flex flex-column h-100">
	   
	   		<!-- Fixed navbar -->
	   		<jsp:include page="../navbar.jsp"></jsp:include>
	    
			
			<!-- Begin page content -->
			<main class="flex-shrink-0">
			  <div class="container">
			  
			  		<div class='card'>
					    <div class='card-header'>
					        <h5>Visualizza dettaglio</h5>
					    </div>
					    
					
					    <div class='card-body'>
					    	<dl class="row">
							  <dt class="col-sm-3 text-right">Elementi Totali nel Database:</dt>
							  <dd class="col-sm-9">${all_list_attribute}</dd>
					    	</dl>
					    	
					    	<dl class="row">
							  <dt class="col-sm-3 text-right">Elementi In Orbita non disattivati:</dt>
							  <dd class="col-sm-9">${emergenze_list_attribute}</dd>
					    	</dl>
					    	
					    	
					    	
					    	
					    </div>
					    <!-- end card body -->
					    
					    			
					    <div class='card-footer'>
					        <a href="${pageContext.request.contextPath}/satellite" class='btn btn-outline-secondary' style='width:80px'>
					            <i class='fa fa-chevron-left'></i> Back
					        </a>
					        
					        
					    </div>
					<!-- end card -->
						
					</div>	
			  <form method="post" action="${pageContext.request.contextPath}/satellite/editEmergenze" novalidate="novalidate">
									<button type="submit" name="submit" id="submit"
									class="btn btn-primary btn-sm">Lancia</button>
						</form>
			    
			  <!-- end container -->  
			  </div>
			  
			</main>
			
			<!-- Footer -->
			<jsp:include page="../footer.jsp" />
	  </body>
</html>