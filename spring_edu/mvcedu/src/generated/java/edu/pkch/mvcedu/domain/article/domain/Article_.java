package edu.pkch.mvcedu.domain.article.domain;

import edu.pkch.mvcedu.domain.user.domain.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Article.class)
public abstract class Article_ {

	public static volatile SingularAttribute<Article, String> contents;
	public static volatile SingularAttribute<Article, User> author;
	public static volatile SingularAttribute<Article, Long> id;
	public static volatile SingularAttribute<Article, String> title;

	public static final String CONTENTS = "contents";
	public static final String AUTHOR = "author";
	public static final String ID = "id";
	public static final String TITLE = "title";

}

