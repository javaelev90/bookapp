<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
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
		<h1>SHOW BOOKS</h1>
		<div class="search_field">
			<input type="text" placeholder="Enter book name or author name"/>
			<button>Search</button>
			<button onclick="window.location.href = '/show_books/show_all'">Show all books</button>
        </div>
        <div class="search_result">
            <c:forEach items="${booksAndAuthors}" var="bookWithAuthors">
                <p>Title: <c:out value = "${bookWithAuthors.book.title}"/></p> 
                <p>Description: <c:out value = "${bookWithAuthors.book.description}"/></p> 
                <c:forEach items="${bookWithAuthors.authors}" var="author">
                    <p>Author: <c:out value = "${author.firstName}"/> <c:out value = "${author.lastName}"/></p>
                </c:forEach>
            </c:forEach>
        </div>
		
	</div>
	
</body>
</html>