const http = require('http');
const fs = require('fs');

const server = http.createServer((req, res) => {
    if(req.url === '/'){
        if(req.method === 'GET'){
            fs.readFile("templates/index.html", (err, data) => {
                if (err) throw err;
                else res.end(data);

            });
        }
    }
    else if (req.url === '/pkch'){
        if(req.method === 'GET'){
            fs.readFile("templates/pkch.html", (err, data) => {
                if(err) throw err;
                else res.end(data);       
            });
        }
    }
});

server.listen(3000, (err) => {
    if (err) console.error("server cause error at starting");
    console.log('server on');
});