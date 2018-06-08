<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
	<script>
		function insertAuthorRow() {
			console.log("worked1");
			$("#firstname").clone().insertBefore("#submit");
			$("#lastname").clone().insertBefore("#submit");
			console.log("worked2");
		}
	</script>
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="/">Library</a>
		</div>
		<ul class="nav navbar-nav">
			<li><a href="/">Home</a></li>
			<li><a href="/show_books">Show books</a></li>
			<li><a href="/add_book">Add book</a></li>
			<li class="active"><a href="/edit_book">Edit book</a></li>
		</ul>
	</div>
	</nav>
	<h1>EDIT BOOK</h1>
	<c:choose>
		<c:when test="${gotContent==true}">
			<form:form class="form-horizontal" method="put" action="/edit_book/">
				<input type="hidden" name="id" value="${bookAndAuthors.book.id}">
				<div class="form-group">
					<label class="control-label col-sm-2" for="title">Title:</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="title"
							value="${bookAndAuthors.book.title}">
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-sm-2" for="description">Description:</label>
					<div class="col-sm-10">
						<input type="text" class="form-control" name="description"
							value="${bookAndAuthors.book.description}">
					</div>
				</div>
				<c:forEach items="${bookAndAuthors.authors}" var="author">
					<div class="form-group" id="firstname">
						<label class="control-label col-sm-2" for="author_firstname">Author
							first name:</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="author_firstname"
								value="${author.firstName}">
						</div>
					</div>
					<div class="form-group" id="lastname">
						<label class="control-label col-sm-2" for="author_lastname">Author
							last name:</label>
						<div class="col-sm-10">
							<input type="text" class="form-control" name="author_lastname"
								value="${author.lastName}">
						</div>
					</div>
				</c:forEach>
				<div class="form-group" id="submit">
					<div class="col-sm-offset-2 col-sm-10">
						<input type="submit" class="btn btn-primary" value="Submit">
						<button type="button" class="btn btn-default"
							onclick="insertAuthorRow()">Add author</button>
					</div>
				</div>
			</form:form>
		</c:when>
		<c:otherwise>
			<form:form class="form-horizontal" method="get" action="/edit_book/">
				<div class="form-group">
					<label class="control-label col-sm-2" for="id">Enter ID for book you want to edit:</label>
					<div class="col-sm-10">
						<input type="number" class="form-control" name="id"
							placeholder="Enter id">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-2 col-sm-10">
						<input type="submit" class="btn btn-primary" value="Submit">					
					</div>
				</div>
			</form:form>
		</c:otherwise>
	</c:choose>
</body>
</html>