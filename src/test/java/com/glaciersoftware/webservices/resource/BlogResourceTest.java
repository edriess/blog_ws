package com.glaciersoftware.webservices.resource;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.glaciersoftware.webservices.bean.Blog;
import com.glaciersoftware.webservices.bean.Error;
import com.glaciersoftware.webservices.service.BlogService;
import com.glaciersoftware.webservices.test.MockHelper;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.mock.MockHttpRequest;
import org.jboss.resteasy.mock.MockHttpResponse;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BlogResourceTest {
	public static final String JSON = "application/json;charset=utf-8";

	@Mock
	private BlogService blogService;

	@Mock
	private HttpServletRequest httpServletRequest;

	@InjectMocks
	private BlogResource blogResource;

	@Before
	public void before() {
		ResteasyProviderFactory.getContextDataMap().put(HttpServletRequest.class, httpServletRequest);
	}


	@Test
	public void should_list_blogs() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(blogResource);

		final Integer blogId = 1;
		final String title = "title";

		final Blog expectedBlog = new Blog() {
			{
				setId(blogId);
				setTitle(title);
			}
		};

		final List<Blog> expectedBlogs = new ArrayList<Blog>() {
			{
				add(expectedBlog);
			}
		};

		final CollectionType resultType = TypeFactory.defaultInstance().constructCollectionType(List.class, Blog.class);
		final String expectedResponse = new ObjectMapper().writer().forType(resultType).writeValueAsString(expectedBlogs);

		when(blogService.listBlogs()).thenReturn(expectedBlogs);

		final MockHttpRequest request = MockHttpRequest.get("/blogs/list");
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(blogService).listBlogs();
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_get_blog() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(blogResource);

		final Integer blogId = 1;
		final String title = "title";

		final Blog expectedBlog = new Blog() {
			{
				setId(blogId);
				setTitle(title);
			}
		};

		final String expectedResponse = new ObjectMapper().writer().forType(Blog.class).writeValueAsString(expectedBlog);

		when(blogService.getBlog(blogId)).thenReturn(expectedBlog);

		final MockHttpRequest request = MockHttpRequest.get("/blogs/" + blogId);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(blogService).getBlog(blogId);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_not_get_missing_blog() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(blogResource);

		final Integer blogId = 1;
		final Error error = new Error() {
			{
				setMessage("Blog not found.");
			}
		};

		final String expectedResponse = new ObjectMapper().writer().forType(Error.class).writeValueAsString(error);

		when(blogService.getBlog(blogId)).thenReturn(null);

		final MockHttpRequest request = MockHttpRequest.get("/blogs/" + blogId);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(blogService).getBlog(blogId);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_find_blog() throws URISyntaxException, JsonParseException, JsonMappingException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(blogResource);

		final Integer blogId = 1;
		final String title = "some title";

		final Blog expectedBlog = new Blog() {
			{
				setId(blogId);
				setTitle(title);
			}
		};

		final List<Blog> blogList = new ArrayList<>();
		blogList.add(expectedBlog);


		final String expectedResponse = new ObjectMapper().writer().forType(Blog.class).writeValueAsString(expectedBlog);

		when(blogService.getBlog(blogId)).thenReturn(expectedBlog);

		final MockHttpRequest request = MockHttpRequest.get("/blogs/" + blogId);
		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(blogService).getBlog(blogId);
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals(expectedResponse, response.getContentAsString());
	}

	@Test
	public void should_add_blog() throws URISyntaxException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(blogResource);

		final Integer blogId = 123;
		final Blog blog = new Blog();
		blog.setId(blogId);
		blog.setTitle("some title");
		blog.setContent("some content");

		final byte[] requestBody = new ObjectMapper().writer().forType(Blog.class).writeValueAsBytes(blog);
		final String expectedResponse = "{Elastic Search is awesome}";
		final MockHttpRequest request = MockHttpRequest.post("/blogs/add")
				.contentType(JSON)
				.content(requestBody);

		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verify(blogService).addBlog(any(Blog.class));
		verify(blogService).listBlogs();
		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
		assertEquals("[]", response.getContentAsString());
	}

	@Test
	public void should_not_add_blog_with_no_id() throws URISyntaxException, IOException {
		final Dispatcher dispatcher = MockHelper.createMockDispatcher(blogResource);

		final Blog blog = new Blog();
		blog.setId(null);
		blog.setTitle("some title");
		blog.setContent("some content");

		final byte[] requestBody = new ObjectMapper().writer().forType(Blog.class).writeValueAsBytes(blog);
		final MockHttpRequest request = MockHttpRequest.post("/blogs/add")
				.contentType(JSON)
				.content(requestBody);

		final MockHttpResponse response = new MockHttpResponse();

		dispatcher.invoke(request, response);

		verifyNoMoreInteractions(MockHelper.allDeclaredMocks(this));

		assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
		assertEquals("{\"message\":\"An id is required on the Blog to add it.\"}", response.getContentAsString());
	}

}
