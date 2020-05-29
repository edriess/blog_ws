package com.glaciersoftware.webservices.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.glaciersoftware.webservices.bean.Blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class BlogServiceTest {

	@InjectMocks
	private BlogService blogService;

	@Test
	public void should_list_blogs() {
		final List<Blog> expectedBlogs = new ArrayList<>();

		final List<Blog> blogs = blogService.listBlogs();

		assertEquals(expectedBlogs, blogs);
	}


	@Test
	public void should_not_fetch_blog() {
		final Integer blogId = 1;

		final Blog blog = blogService.getBlog(blogId);

		assertNull(blog);
	}

	@Test
	public void should_add_blog() {
		final Integer blogId = 123;
		final Blog expectedBlog = new Blog();
		expectedBlog.setId(blogId);

		final List<Blog> blogList = new ArrayList<>();
		blogList.add(expectedBlog);

		blogService.addBlog(expectedBlog);

		final List<Blog> foundBlogList = blogService.listBlogs();
		assertEquals(1, foundBlogList.size());
		final Blog newBlog = foundBlogList.get(0);

		assertEquals(expectedBlog, newBlog);
	}

	@Test
	public void should_not_add_blog_with_no_id() {
		final Integer blogId = null;
		final Blog expectedBlog = new Blog();
		expectedBlog.setId(blogId);

		final List<Blog> blogList = new ArrayList<>();
		blogList.add(expectedBlog);

		blogService.addBlog(expectedBlog);

		final List<Blog> foundBlogList = blogService.listBlogs();
		assertEquals(0, foundBlogList.size());
	}

	@Test
	public void should_update_existing_blog() {
		final Integer blogId = 123;
		final Blog expectedBlog = new Blog();
		expectedBlog.setId(blogId);


		final List<Blog> blogList = new ArrayList<>();
		blogList.add(expectedBlog);

		blogService.addBlog(expectedBlog);

		expectedBlog.setTitle("add a new title");
		blogService.addBlog(expectedBlog);

		final List<Blog> foundBlogList = blogService.listBlogs();
		assertEquals(1, foundBlogList.size());
		final Blog newBlog = foundBlogList.get(0);

		assertEquals(expectedBlog, newBlog);
	}


}
