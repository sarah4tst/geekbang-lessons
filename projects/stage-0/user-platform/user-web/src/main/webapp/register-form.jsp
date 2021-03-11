<head>
  <jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
  <title>Register Page</title>
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
    <form class="form-signup" method="post" action="/register" >
      <h1 class="h3 mb-3 font-weight-normal">注册</h1>
      <input id="name" name="name" class="form-control" placeholder="用户名" required autofocus />
      <input id="inputEmail" name="email" class="form-control" placeholder="请输入电子邮件" />
      <input type="password" id="inputPassword" name="password" class="form-control" value="123456" placeholder="请输入密码"  required />
      <input type="tel" id="phoneNumber" name="phoneNumber"  class="form-control" value="12345678901" placeholder="电话号码" />
      <button class="btn btn-lg btn-primary btn-block" type="submit">Sign
        Up</button>
    </form>
    <p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
  </div>
</body>
