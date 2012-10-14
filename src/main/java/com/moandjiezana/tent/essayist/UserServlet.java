package com.moandjiezana.tent.essayist;

import com.moandjiezana.tent.client.TentClient;
import com.moandjiezana.tent.client.posts.Post;
import com.moandjiezana.tent.client.posts.PostQuery;
import com.moandjiezana.tent.essayist.tent.Entities;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class UserServlet extends HttpServlet {
  
  private final Templates templates;

  @Inject
  public UserServlet(Templates templates) {
    this.templates = templates;
  }
  
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String entity = req.getPathInfo();
    if (entity.startsWith("/")) {
      entity = entity.substring(1);
    }
    
    TentClient tentClient = new TentClient(Entities.expandFromUrl(entity));
    tentClient.discover();
    tentClient.getProfile();
    
    List<Post> essays = tentClient.getPosts(new PostQuery().postTypes(Post.Types.essay("v0.1.0")));
    
    templates.essays().render(resp.getWriter(), essays);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.sendRedirect(req.getContextPath() + "/" + Entities.getEntityForUrl(req.getParameter("entity")) + "/essays");
  }
}
