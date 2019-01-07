# 2019 01.07 Monday

1. Node.js

    - ejs 템플릿 엔진 사용하기

    ```javascript
    let template = ejs.compile(str, options);
    template(data);
    // => Rendered HTML string

    ejs.render(str, data, options);
    // => Rendered HTML string

    ejs.renderFile(filename, data, options, function(err, str){
        // str => Rendered HTML string
    });
    ```
    다음과 같은 방법으로 ejs 엔진을 사용하여 render 할 수 있다.

    `express`에서는 `res` 응답 객체에 `render` 함수를 통해 템플릿 엔진으로 데이터를 넘겨 줄 수 있다.

    ```javascript
    // app.js
    app.use('/', (req, res) => {
        return res.render('index', { title: 'Express Practice' })
    });
    // index.ejs
    <!DOCTYPE html>
    <html>
        <head>
            <title><%= title %></title>
            <link rel='stylesheet' href='/stylesheets/style.css' />
        </head>
        <body>
            <h1><%= title %></h1>
            <p>Welcome to <%= title %></p>
        </body>
    </html>
    ```
    app.js에서 루트`/` 경로로 요청을 보내면 `index.html` (index.ejs가 index.html을 만들어줌)을 보여준다. 이 때, title이라는 값을 주어 `index.ejs`로 보내면 `<%= %>`로 된 표현식에서 값을 rendering한다.

    > ejs 태그 종류
    >
    > - <% %> : 스크립트릿 태그, output은 없으며 control-flow를 위해 사용한다. 또한 내부 변수를 줄 수도 있다.
    > - <%- -%> : Trim-mode로 개행 없이 출력하는 태그, escape 없이 그대로 출력하는데 사용
    > - <%_ _%> : whitespace sluping 태그, 모든 공백을 제거하는 태그
    > - <%# %> : 주석

    위의 `<%- -%>` 태그를 이용하여 `include`를 할 수 있다.

    ```html
    <ul>
        <% users.forEach(function(user){ %>
            <%- include('user/show', {user: user}); %>
        <% }); %>
    </ul>
    ```

    이렇게 user 이름만 바꿔서 user 폴더 내부의 `show.ejs`를 가져와 랜더링할 수 있다.

    - 