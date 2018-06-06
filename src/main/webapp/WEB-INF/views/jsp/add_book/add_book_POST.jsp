<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/resources/css/general_style.css">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Personal Library</title>
</head>
<body>
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="/">Library</a>
		</div>
		<ul class="nav navbar-nav">
			<li><a href="/">Home</a></li>
			<li><a href="/show_books">Show books</a></li>
			<li class="active"><a href="/add_book">Add book</a></li>
			<li><a href="/edit_book">Edit book</a></li>
		</ul>
	</div>
	</nav>
	<div id="content">
		<c:choose>
			<c:when test="${response_code eq -1}">
				<h2>Could not add book</h2>
			</c:when>
			<c:otherwise>
				<h2>Successfully added book</h2>
				<p>Title: ${title}</p>
				<p>Description: ${description}</p>
				<p>Author: ${author_firstname} ${author_lastname}</p>
			</c:otherwise>
		</c:choose>	
		
	</div>
</body>
</html>