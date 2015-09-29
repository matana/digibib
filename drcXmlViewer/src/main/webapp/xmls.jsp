<%@ page pageEncoding="utf-8"%>
<%@ page import="java.util.List"%>
<%
	String volume = (String) request.getAttribute("volume");
	String volumeName = (String) request.getAttribute("volumeName");
	String chapterSelected = (String) request
			.getAttribute("chapterSelected");
	List<String> pageNumbers = (List<String>) request
			.getAttribute("pageNumbers");
	List<String> chapters = (List<String>) request
			.getAttribute("chapters");
	List<String> xmls = (List<String>) request.getAttribute("xmls");
	String toDisplay = (String) request.getAttribute("toDisplay");
	String xmlDisplay = (String) request.getAttribute("xmlDisplay");
	String pageNumber = (String) request.getAttribute("pageNumber");
	String selected = (String) request.getAttribute("selected");
	String lastSearch = (String) request.getAttribute("lastSearch");

	String title = "";
	if (selected != null) {
		title = " - " + selected;
	}
%>


<jsp:include page="/modules/htmlhead.jsp" />

<body>

	<div id="top">
		<jsp:include page="/modules/drc_header_small.jsp" />
	</div>

	<!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>

	<div class="container-fluid">

		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		<div class="row-fluid">
			<div class="page-header text-center">
				<h1>
					<a style="color: black;" href="/drc">Rätoromanische
						Chrestomathie <small>von Caspar Decurtins</small>
					</a>
				</h1>
			</div>
		</div>
		<div class="row-fluid">
			<div class="span12"></div>
		</div>


		<div class="row-fluid">
			<div class="span3 well">
				<legend>
					Band:
					<%=(volume != null) ? volumeName.split("-")[0] : "-"%>
					<p>
						Seite:
						<%=(pageNumber != null) ? pageNumber : "-"%>
						<a class="btn btn-small pull-right" href="/drc">Zur
							Bandauswahl<i class="icon-book"></i>
						</a>
						<!-- 
						<a class="btn btn-small pull-right" href="/">Zurück zur
							Übersicht</a>
					 -->
					</p>
				</legend>

				<ul class="nav nav-list">
					<li class="nav-header">Wählen Sie ein Kapitel:</li>

					<%
						for (String chapter : chapters) {
					%>
					<li
						<%=(chapterSelected != null && chapterSelected
						.compareTo(chapter) == 0) ? "class=\"active\"" : ""%>>
						<a
						href="/chapterSelected?chapter=<%=chapter.replaceAll(" ", "+")%>&volume=<%=volume%>">
							<%=chapter%>
					</a>
					</li>
					<%
						}
					%>
					<legend></legend>
					<li class="nav-header">Oder geben Sie eine Seite ein:</li>
				</ul>

				<form action="xmlSelected" method="get">
					<fieldset>
						<div class="input-small input-append">
							<input type="text" name="pageNumber" placeholder="Seitenzahl">
							<button type="submit" class="btn">Los!</button>
						</div>
						<span class="help-block">In Band <%=volumeName.split("-")[0]%>
							liegen die Seitenzahlen zwischen <%=pageNumbers.get(0)%> und <%=pageNumbers.get(pageNumbers.size() - 1)%>.
						</span> </span> <input type="hidden" name="volume" value=<%=volume%>>
					</fieldset>
				</form>
			</div>
			<%
				if (toDisplay != null) {
			%>
			<div class="span6 well">
			<!--  	<div><%=toDisplay%></div> -->
			<ul id="tabs" class="nav nav-tabs">
				    <li class="active"><a href="#tabPlain" data-toggle="tab">Volltext</a></li>
				    <li><a href="#tabXML" data-toggle="tab">XML</a></li>
			    </ul>
			    <div class="tab-content">
				    <div class="tab-pane active" id="tabPlain">
				    	<div class="">
							<div><%=toDisplay%></div>
						</div>
				    </div>
				    <div class="tab-pane" id="tabXML">
				    	<div class="">
							<div><%=xmlDisplay%></div>
						</div>
				    </div>
			    </div>
			</div>

			<%
				int index = pageNumbers.indexOf(pageNumber);
					String prev = "";
					String next = "";
					String prevDis = "";
					String nextDis = "";
					if (index > 0) {
						prev = "/xmlSelected?pageNumber="
								+ pageNumbers.get(index - 1) + "&volume=" + volume;
					} else {
						prev = "";
						prevDis = "disabled";
					}
					if (index < xmls.size() - 1) {
						next = "/xmlSelected?pageNumber="
								+ pageNumbers.get(index + 1) + "&volume=" + volume;
					} else {
						next = "";
						nextDis = "disabled";
					}
			%>

			<div class="span2">
				<legend>
					<div class="btn-group btn-block">
						<a class="btn <%=prevDis%>"
							href="<%=prev%><%=(lastSearch != null) ? "&lastSearch=" + lastSearch
						: ""%>"><i
							class="icon-chevron-left"></i>prev</a> <a class="btn <%=nextDis%>"
							href="<%=next%><%=(lastSearch != null) ? "&lastSearch=" + lastSearch
						: ""%>">next<i
							class="icon-chevron-right"></i></a>
					</div>
				</legend>

				<%
					if (toDisplay != null) {
				%>
				<p>
					<a class="btn btn-block" href="/downloadXml?xmls=<%=selected%>">
						Seite speichern <i class="icon-download"></i>
					</a>
				</p>
				<%
					}
				%>
				<%
					if (lastSearch != null) {
				%>
				<p>
					<a class="btn btn-block"
						href="/search.html?search=<%=lastSearch.split("_")[0]%>&resultPage=<%=lastSearch.split("_")[1]%>">
						Zum letzten Suchergebnis </a>
				</p>
				<%
					}
				%>
			</div>
			<%
				}
			%>

		</div>
	</div>
</body>
</html>
