package com.glaciersoftware.webservices.resource;

import com.glaciersoftware.webservices.bean.Blog;
import com.glaciersoftware.webservices.bean.Error;
import com.glaciersoftware.webservices.service.BlogService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import java.util.List;

import io.swagger.annotations.ApiParam;

@Service
@Path("/blogs")
public class BlogResource {
	public static final String JSON = "application/json;charset=utf-8";
	private final BlogService blogService;

	@Autowired
	public BlogResource(
			final BlogService blogService) {
		this.blogService = blogService;
	}

	@Path("/add")
	@POST
	@Produces(JSON)
	public Response addBlog(
			@ApiParam(hidden = true) @HeaderParam(HttpHeaders.ACCEPT) final String mediaType,
			@ApiParam(hidden = true) @Context final HttpServletRequest request,
			@ApiParam(required = true) final Blog blog) {

		if (blog.getId() != null) {
			blogService.addBlog(blog);
			final List<Blog> blogs = blogService.listBlogs();
			return Response.status(Response.Status.OK).entity(blogs).build();
		} else {
			final Error error = new Error();
			error.setMessage("An id is required on the Blog to add it.");
			return Response.status(Response.Status.BAD_REQUEST).entity(error).build();
		}
	}

	@Path("/{blogId}")
	@GET
	@Produces(JSON)
	public Response getUser(@PathParam("blogId") final Integer blogId) {
		final Blog blog = blogService.getBlog(blogId);

		if (null == blog) {
			final Error error = new Error();
			error.setMessage("Blog not found.");
			return Response.status(Response.Status.NOT_FOUND).entity(error).build();
		} else {
			return Response.status(Response.Status.OK).entity(blog).build();
		}
	}

	@Path("/list")
	@GET
	@Produces(JSON)
	public Response listBlogs() {

		final List<Blog> blogs = blogService.listBlogs();

		return Response.status(Response.Status.OK).entity(blogs).build();
	}

}
