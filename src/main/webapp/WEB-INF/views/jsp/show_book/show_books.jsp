<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/resources/css/general_style.css">
<link rel="stylesheet" href="/resources/css/show_books.css">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
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
				<li class="active"><a href="/show_books">Show books</a></li>
				<li><a href="/add_book">Add book</a></li>
				<li><a href="/edit_book">Edit book</a></li>
			</ul>
		</div>
	</nav>
	<div id="content">
		<div id="function-area">
			<h1>SHOW BOOKS</h1>	
			<div class="search_field">
				<form method="GET" action="/show_books">
					<input name="search_string" type="text" placeholder="Enter book name or author name"/>
					<input class="btn btn-primary" type="submit" value="Search">
				</form>
				<button class="btn btn-primary" onclick="window.location.href = '/show_books/show_all'">Show all books</button>
			</div>
		</div>
		
	</div>
	
</body>
</html>