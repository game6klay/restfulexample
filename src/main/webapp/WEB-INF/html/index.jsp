<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
    <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>URL Shortner</title>
    <meta charset="utf-8">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdn.datatables.net/1.10.15/js/jquery.dataTables.min.js"></script>
    <link rel="stylesheet" href="https://cdn.datatables.net/1.10.15/css/jquery.dataTables.min.css" />
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.4/angular.min.js"></script>
    <script src="js/urlShortner.js"></script>
</head>
<body ng-app="urlShortner" ng-controller="shortnerController">
    <div class="container-fluid">
        <nav class="navbar navbar-default" style="background-color:white;margin-bottom:0px;">
            <div class="container-fluid">
                <div class="col-md-offset-5">
                    <a class="navbar-brand">LogicMonitor URL Shortner</a>
                </div>
            </div>
        </nav>
        <div class="jumbotron col-md-12" style="background-color:teal;color:white;">
            <form ng-submit="shortenURL();" name="urlForm">
                <div class="row">
                    <div class="col-md-3 col-md-offset-2">
                        <h2>Simplify your links</h2>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6 col-md-offset-2">
                        <input type="url" class="form-control" ng-model="originalURL" placeholder="Your original URL here" ng-required="true" />
                    </div>
                    <div class="col-md-1">
                        <input type="submit" style="color:teal;" value="SHORTEN URL" class="btn btn-default" />
                    </div>
                    <div class="col-md-1" style="margin-left:35px;">
                        <input type="button" style="color:teal;" value="LENGTHEN URL" class="btn btn-default" ng-click="lengthenURL(urlForm.$valid);" />
                    </div>
                    <div class="col-md-1" style="margin-left:35px;">
                        <input type="button" style="color:teal;" value="BLACKLIST URL" class="btn btn-default" ng-click="blacklistURL(urlForm.$valid);" />
                    </div>
                </div>
            </form>
        </div>
        <div class="row">
            <div class="col-md-8 col-md-offset-2">
                <table class="table table-striped" id="urlTable">
                	<thead>
                	<th> Original URL</th>
                		<th>Shorten URL</th>
                	</tr>
                		
                	</thead>
                	<c:forEach var="document" items="${allURLs}">
                	<tr>
    					<td><a href="${document.getLongURL()}" target="_blank">${document.getLongURL()}</a></td>
    					<td><a href="rd?to=${document.getShortURL()}" target="_blank">${document.getShortURL()}</a></td>
    					</tr>
					</c:forEach>
                </table>
            </div>
        </div>
        <br />
        <footer>
            <div class="panel panel-default">
                <div class="panel-footer col">
                    <div class="col-md-offset-2">
                        © Jay Patel
                    </div>
                </div>
            </div>
        </footer>
    </div>
</body>
</html>