<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="/resources/css/general_style.css">
<link rel="stylesheet" href="/resources/css/add_book_style.css">
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
		function insertAuthorRow(){
			$( "#firstname" ).clone().insertBefore( "#submit" );
			$( "#lastname" ).clone().insertBefore( "#submit" );
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
			<li class="active"><a href="/add_book">Add book</a></li>
			<li><a href="/edit_book">Edit book</a></li>
		</ul>
	</div>
	</nav>
	<div id="content">
		<h1>ADD BOOK</h1>
		<form class="form-horizontal" method="post" action="/add_book"
			onSubmit="">
			<div class="form-group">
				<label class="control-label col-sm-2" for="title">Title:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="title"
						placeholder="Enter title">
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-sm-2" for="description">Description:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="description"
						placeholder="Enter description">
				</div>
			</div>
			<div class="form-group" id="firstname">
				<label class="control-label col-sm-2" for="author_firstname">Author
					first name:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="author_firstname"
						placeholder="Enter author first name">
				</div>
			</div>
			<div class="form-group" id="lastname">
				<label class="control-label col-sm-2" for="author_lastname">Author
					last name:</label>
				<div class="col-sm-10">
					<input type="text" class="form-control" name="author_lastname"
						placeholder="Enter author last name">
				</div>
			</div>
			
			<div class="form-group" id="submit">
				<div class="col-sm-offset-2 col-sm-10">
					<button type="submit" class="btn btn-primary">Submit</button>
					<button type="button" class="btn btn-default" onclick="insertAuthorRow()">Add author</button>
				</div>
			</div>
			
		</form>
	</div>
</body>
</html>