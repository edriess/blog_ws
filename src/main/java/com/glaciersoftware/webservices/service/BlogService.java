package com.glaciersoftware.webservices.service;

import com.glaciersoftware.webservices.bean.Blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogService {

	private final Map<Integer, Blog> blogMap = new HashMap<>();

	@Autowired
	public BlogService() {
	}

	public List<Blog> listBlogs() {
		return new ArrayList(blogMap.values());
	}

	public Blog getBlog(final Integer id) {
		return blogMap.get(id);
	}

	public void addBlog(final Blog blog) {
		if (blog != null && blog.getId() != null) {
			final Blog existingBlog = blogMap.get(blog.getId());
			if (existingBlog == null) {
				blogMap.put(blog.getId(), blog);
			} else {
				if (existingBlog.getId() != null) {
					blogMap.remove(existingBlog.getId());
					blogMap.put(blog.getId(), blog);
				}
			}
		}

	}

}
