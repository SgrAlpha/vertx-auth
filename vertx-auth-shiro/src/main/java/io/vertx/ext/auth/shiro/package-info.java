/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

/**
 *
 * == The Apache Shiro Auth provider implementation
 *
 * This is an auth provider implementation that uses http://shiro.apache.org/[Apache Shiro].  To use this
 * project, add the following dependency to the _dependencies_ section of your build descriptor:
 *
 * * Maven (in your `pom.xml`):
 *
 * [source,xml,subs="+attributes"]
 * ----
 * <dependency>
 *   <groupId>${maven.groupId}</groupId>
 *   <artifactId>${maven.artifactId}</artifactId>
 *   <version>${maven.version}</version>
 * </dependency>
 * ----
 *
 * * Gradle (in your `build.gradle` file):
 *
 * [source,groovy,subs="+attributes"]
 * ----
 * compile ${maven.groupId}:${maven.artifactId}:${maven.version}
 * ----
 *
 * We provide out of the box support for properties and LDAP based auth using Shiro, and you can also plugin in any
 * other Shiro Realm which expects username and password for credentials.
 *
 * To create an instance of the provider you use {@link io.vertx.ext.auth.shiro.ShiroAuth}. You specify the type of
 * Shiro auth provider that you want with {@link io.vertx.ext.auth.shiro.ShiroAuthRealmType}, and you specify the
 * configuration in a JSON object.
 *
 * Here's an example of creating a Shiro auth provider by specifying the type:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example3}
 * ----
 *
 * == Authentication
 *
 * When authenticating using this implementation, it assumes `username` and `password` fields are present in the
 * authentication info:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example4}
 * ----
 *
 * == Authorisation - Permission-Role Model
 *
 * Although Vert.x auth itself does not mandate any specific model of permissions (they are just opaque strings), this
 * implementation uses a familiar user/role/permission model, where a user can have zero or more roles and a role
 * can have zero or more permissions.
 *
 * If validating if a user has a particular permission simply pass the permission into.
 * {@link io.vertx.ext.auth.User#isAuthorised(java.lang.String, io.vertx.core.Handler)} as follows:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example5}
 * ----
 * If validating that a user has a particular _role_ then you should prefix the argument with the role prefix.
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example6}
 * ----
 *
 * The default role prefix is `role:`. You can change this with {@link io.vertx.ext.auth.shiro.ShiroAuth#setRolePrefix(java.lang.String)}.
 *
 * === The Shiro properties auth provider
 *
 * This auth provider implementation uses Apache Shiro to get user/role/permission information from a properties file.
 *
 * Note that roles are not available directly on the API due to the fact that vertx-auth tries to be as portable as
 * possible. However one can run assertions on role by using the prefix `role:` or by specifying the prefered prefix
 * with {@link io.vertx.ext.auth.shiro.ShiroAuth#setRolePrefix(java.lang.String)}.
 *
 * The implementation will, by default, look for a file called `vertx-users.properties` on the classpath.
 *
 * If you want to change this, you can use the `properties_path` configuration element to define how the properties
 * file is found.
 *
 * The default value is `classpath:vertx-users.properties`.
 *
 * If the value is prefixed with `classpath:` then the classpath will be searched for a properties file of that name.
 *
 * If the value is prefixed with `file:` then it specifies a file on the file system.
 *
 * If the value is prefixed with `url:` then it specifies a URL from where to load the properties.
 *
 * The properties file should have the following structure:
 *
 * Each line should either contain the username, password and roles for a user or the permissions in a role.
 *
 * For a user line it should be of the form:
 *
 *  user.{username}={password},{roleName1},{roleName2},...,{roleNameN}
 *
 * For a role line it should be of the form:
 *
 *  role.{roleName}={permissionName1},{permissionName2},...,{permissionNameN}
 *
 * Here's an example:
 * ----
 * user.tim = mypassword,administrator,developer
 * user.bob = hispassword,developer
 * user.joe = anotherpassword,manager
 * role.administrator=*
 * role.manager=play_golf,say_buzzwords
 * role.developer=do_actual_work
 * ----
 *
 * When describing roles a wildcard `*` can be used to indicate that the role has all permissions.
 *
 * === The Shiro LDAP auth provider
 *
 * The LDAP auth realm gets user/role/permission information from an LDAP server.
 *
 * The following configuration properties are used to configure the LDAP realm:
 *
 * `ldap-user-dn-template`:: this is used to determine the actual lookup to use when looking up a user with a particular
 * id. An example is `uid={0},ou=users,dc=foo,dc=com` - the element `{0}` is substituted with the user id to create the
 * actual lookup. This setting is mandatory.
 * `ldap_url`:: the url to the LDAP server. The url must start with `ldap://` and a port must be specified.
 * An example is `ldap://myldapserver.mycompany.com:10389`
 * `ldap-authentication-mechanism`:: TODO
 * `ldap-context-factory-class-name`:: TODO
 * `ldap-pooling-enabled`:: TODO
 * `ldap-referral`:: TODO
 * `ldap-system-username`:: TODO
 * `ldap-system-password`:: TODO
 *
 * === Using another Shiro Realm
 *
 * It's also possible to create an auth provider instance using a pre-created Apache Shiro Realm object.
 *
 * This is done as follows:
 *
 * [source,java]
 * ----
 * {@link examples.Examples#example8}
 * ----
 *
 * The implementation currently assumes that user/password based authentication is used.
 *
 * @author <a href="mailto:julien@julienviet.com">Julien Viet</a>
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
@Document(fileName = "index.adoc")
@ModuleGen(name = "vertx-auth-shiro", groupPackage = "io.vertx")
package io.vertx.ext.auth.shiro;

import io.vertx.codegen.annotations.ModuleGen;
import io.vertx.docgen.Document;
