<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet"
	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
	crossorigin="anonymous">
<title>Dashboard Page</title>
<link rel="stylesheet" type="text/css" href="/css/style.css">
<script type="text/javascript" src="js/app.js"></script>
<!-- for Bootstrap CSS -->
<link rel="stylesheet" href="/webjars/bootstrap/css/bootstrap.min.css" />
<!-- For any Bootstrap that uses JS or jQuery-->
<script src="/webjars/jquery/jquery.min.js"></script>
</head>
<body>
	<div class="container">
		<div class="row">
			<div class="col mt-3">
		  		<p>Hello, <c:out value="${loggedInUser.userName}" /> Welcome to</p>
  			</div>
		  	<div class="col mt-3 text-end">
		  		<a href="/logout">Log out</a>
			</div>
		</div>
		<div class="row">
			<div class="col">
				<h4>The Book Broker!</h4>
			</div>
		</div>
		<div class="row">
			<div class="col mt-3">
				<p>Available Books to Borrow</p>
			</div>
			<div class="col mt-3 text-end">
		  		<a href="/books/new">+ Add book</a>
			</div>
		</div>
		<div class="row">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th scope="col">#</th>
						<th scope="col">Title</th>
						<th scope="col">Author Name</th>
						<th scope="col">Owner</th>
						<th scope="col">Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="book" items="${books}">
						<tr>
							<td>${book.id}</td>
							<td><a href="books/${book.id}">${book.title}</a></td>
							<td>${book.author}</td>
							<td>${book.user.userName}</td>
							<c:choose>
								<c:when test="${loggedInUser.id == book.user.id}">
									<td><a href="books/${book.id}/edit">Edit</a> 
										||
										<a href="books/${book.id}/edit">Delete</a>
									</td>
								</c:when>
								<c:otherwise>
									<td><a href="bookmarket/${book.id}/borrow">Borrow</a></td>
								</c:otherwise>
							</c:choose>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div class="row">
			<p class="mt-3">Books I am Borrowing...</p>
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<th scope="col">#</th>
						<th scope="col">Title</th>
						<th scope="col">Author Name</th>
						<th scope="col">Owner</th>
					<th scope="col">Action</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="book" items="${books}">
						<c:if test="${loggedInUser.id == book.borrower.id}">
							<tr>
								<td>${book.id}</td>
								<td><a href="books/${book.id}">${book.title}</a></td>
								<td>${book.author}</td>
								<td>${book.user.userName}</td>
								<td><a href="bookmarket/${book.id}/return">Return</a></td>
							</tr>
						</c:if>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>