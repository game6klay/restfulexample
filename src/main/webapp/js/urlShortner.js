var urlShortner = angular.module('urlShortner', []);

urlShortner.controller('shortnerController', function ($scope, $http) {
	 $scope.originalURL=undefined;
    $scope.shortenURL = function () {
        $http({
            method: 'POST',
            url: 'rest/urlService/urlShortner',
            data: { "url": $scope.originalURL,"shorten":true,"blacklist":false}
        }).success(function (data) {
        	var table = document.getElementById("urlTable");
            var row = table.insertRow(1);
            var cell1 = row.insertCell(0);
            var link = document.createElement("a");
            link.setAttribute("href", $scope.originalURL);
            link.setAttribute("target", "_blank");
            var linkText = document.createTextNode($scope.originalURL);
            link.appendChild(linkText);
            cell1.appendChild(link);
            var cell2 = row.insertCell(1);
            link = document.createElement("a");
            link.setAttribute("href", "rd?to="+data.shortURL);
            link.setAttribute("target", "_blank");
            var linkText = document.createTextNode(data.shortURL);
            link.appendChild(linkText);
            cell2.appendChild(link);
        }).error(function (data) {
        	alert("error: "+data);
        });
    }
    $scope.lengthenURL = function (valid) {
    	if(!valid){
    		alert('Invalid URL');
    		return false;
    	}
        $http({
            method: 'POST',
            url: 'rest/urlService/urlShortner',
            data: { "url": $scope.originalURL,"shorten":false,"blacklist":false}
        }).success(function (data) {
        	alert(data.longURL);
        }).error(function (data) {
        	alert("Lengthen error: "+data);
        });
    }
    
    $scope.blacklistURL = function (valid) {
    	if(!valid){
    		alert('Invalid URL');
    		return false;
    	}
        $http({
            method: 'POST',
            url: 'rest/urlService/urlShortner',
            data: { "url": $scope.originalURL,"shorten":false,"blacklist":true}
        }).success(function (data) {
        	alert('URL Blocklisted Successfully');
        }).error(function (data) {
        	alert("Lengthen error: "+data);
        });
    }

});
