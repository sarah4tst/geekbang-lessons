<head>
  <jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
  <title>My Home Page</title>
  <style>
    .bd-placeholder-img {
      font-size: 1.125rem;
      text-anchor: middle;
      -webkit-user-select: none;
      -moz-user-select: none;
      -ms-user-select: none;
      user-select: none;
    }

    @media (min-width: 768px) {
      .bd-placeholder-img-lg {
        font-size: 3.5rem;
      }
    }
  </style>
</head>

<body>
  <div class="container">
    <form class="form-signin" action="/login" method="post">
      <h1 class="h3 mb-3 font-weight-normal">登录</h1>
      <input name="name" id="inputName" class="form-control" placeholder="请输入用户名" required autofocus />
      <input type="password" name="password" id="inputPassword" class="form-control" value="123456" placeholder="请输入密码" required />
      <div class="checkbox mb-3">
        <label> <input type="checkbox" value="remember-me">
          Remember me
        </label>
      </div>
      <button class="btn btn-lg btn-primary btn-block" type="submit">Sign
        in</button>
    </form>
    <p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
  </div>
</body>
