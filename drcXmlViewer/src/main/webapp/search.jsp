<%@ page pageEncoding="utf-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%
	String searchPhrase = (String) request.getAttribute("searchPhrase");
	Map<String, String> result = (Map<String, String>) request
			.getAttribute("result");
	int resultPage = (Integer) request.getAttribute("resultPage");
	int totalHits = (Integer) request.getAttribute("totalHits");
	int offset = (Integer) request.getAttribute("offset");
	String toHighlight = (String) request.getAttribute("toHighlight");
%>

<jsp:include page="/modules/htmlhead.jsp" />

<body>
	<div id="top">
		<jsp:include page="/modules/drc_header_small.jsp" />
	</div>

	<div class="container-fluid">

		<div class="row-fluid">
			<div class="span12"></div>
		</div>
		<div class="row-fluid">
			<div class="page-header text-center">
				<h1>
					<a style="color: black;" href="/volumeSelectionDrc">Rätoromanische
						Chrestomathie </a> <small>Suchergebnis für <em
						class="text-info">"<%=searchPhrase%>"
					</em></small>
				</h1>
			</div>
		</div>
	</div>

	<div class="row-fluid">
		<div class="span12"></div>
	</div>

	<div class="row-fluid">

		<div class="span3 well">
			<legend>
				Suchwort: <em class="text-info"><strong><%=searchPhrase%></strong></em>
				<p>
					Treffer <em class="text-info"><%=offset + 1%>-<%=offset + result.size()%></em>
					von <em class="text-info"><%=totalHits%></em>
				</p>
			</legend>
			<p class="lead">Neue Suche:</p>

			<form class="navbar-search pull-left" action="/search.html">
				<input type="text" class="search-query" placeholder="Search"
					name="search" value="">
			</form>

		</div>

		<%
			if (result.size() == 0) {
		%>
		<div class="span6">
			<div class="lead text-center text-warning">
				<em>Keine Treffer gefunden!</em>
			</div>
		</div>

		<%
			} else if (result == null) {
				System.out.println("result is null");
			} else {
		%>
		<div class="span6 well">


			<%
				if (totalHits > 10) {
			%>
			<div class="span12 pagination pagination-centered">
				<ul>
					<li <%=(resultPage == 1) ? "class=\"disabled\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=1%>">Erste</a></li>
					<li <%=(resultPage == 1) ? "class=\"disabled\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=(resultPage > 1) ? resultPage - 1 : resultPage%>">&laquo;</a></li>

					<%
						int range = 4;
								int last = (totalHits % 10 == 0) ? totalHits / 10
										: totalHits / 10 + 1;
								int min = Math.max(1, resultPage - range);
								int max = Math.min(resultPage + range, last);

								if (max < resultPage + range) {
									min -= (resultPage + range) - max;
									min = Math.max(1, min);
								}

								for (int i = min; i < resultPage; i++) {
					%>
					<li><a href="/search.html?search=<%=searchPhrase%>&resultPage=<%=i%>"><%=i%></a></li>
					<%
						}
					%>

					<li class="active"><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=resultPage%>"><%=resultPage%></a></li>

					<%
						if (min > resultPage - range) {
									max += range - resultPage + 1;
									max = Math.min(max, last);
								}

								for (int i = resultPage + 1; i <= max; i++) {
					%>
					<li><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=i%>"><%=i%></a></li>
					<%
						}
					%>

					<li <%=(resultPage == last) ? "class=\"disabled\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=!(resultPage > last - 1) ? resultPage + 1
							: resultPage%>">&raquo;</a></li>
					<li <%=(resultPage == last) ? "class=\"active\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=last%>">Letzte</a></li>


				</ul>
			</div>
			<%
				}
			%>


			<ul class="nav nav-list">
				<li class="nav-header"></li>

				<%
					for (String key : result.keySet()) {
				%>
				<div class="span12 well">

					<form action="xmlSelected" method="get">

						Band
						<%=key.split("-")[0]%>, Seite
						<%=key.split("-")[1]%>: <input type="submit"
							class="btn btn-link pull-right" value="Seite anzeigen"> <input
							type="hidden" name="pageNumber" value="<%=key.split("-")[1]%>">
						<input type="hidden" name="volume" value="<%=key.split("-")[2]%>">
						<input type="hidden" name="lastSearch"
							value="<%=searchPhrase%>_<%=resultPage%>"> <input
							type="hidden" name="toHighlight" value="<%=searchPhrase%>">
					</form>

					<legend> </legend>
					<li><%=result.get(key)%></li>
				</div>
				<%
					}
				%>
			</ul>


			<%
				if (totalHits > 10) {
			%>
			<div class="span12 pagination pagination-centered">
				<ul>
					<li <%=(resultPage == 1) ? "class=\"disabled\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=1%>">Erste</a></li>
					<li <%=(resultPage == 1) ? "class=\"disabled\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=(resultPage > 1) ? resultPage - 1 : resultPage%>">&laquo;</a></li>

					<%
						int range = 4;
								int last = (totalHits % 10 == 0) ? totalHits / 10
										: totalHits / 10 + 1;
								int min = Math.max(1, resultPage - range);
								int max = Math.min(resultPage + range, last);

								if (max < resultPage + range) {
									min -= (resultPage + range) - max;
									min = Math.max(1, min);
								}

								for (int i = min; i < resultPage; i++) {
					%>
					<li><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=i%>"><%=i%></a></li>
					<%
						}
					%>

					<li class="active"><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=resultPage%>"><%=resultPage%></a></li>

					<%
						if (min > resultPage - range) {
									max += range - resultPage + 1;
									max = Math.min(max, last);
								}

								for (int i = resultPage + 1; i <= max; i++) {
					%>
					<li><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=i%>"><%=i%></a></li>
					<%
						}
					%>

					<li <%=(resultPage == last) ? "class=\"disabled\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=!(resultPage > last - 1) ? resultPage + 1
							: resultPage%>">&raquo;</a></li>
					<li <%=(resultPage == last) ? "class=\"active\"" : ""%>><a
						href="/search.html?search=<%=searchPhrase%>&resultPage=<%=last%>">Letzte</a></li>


				</ul>
			</div>
			<%
				}
				}
			%>
		</div>
</body>
</html>