const path = require('path');
const express = require('express');
const http = require('http');
const logger = require('morgan');

const port = process.env.NODE_ENV || 3000;
const app = express();

app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(logger('dev'));
app.use(express.static(path.join(__dirname, 'public')));

app.get('/', (req, res) => {
   res.render('index', {title: 'socket.io test'})
});

const server = http.Server(app);
const io = require('socket.io').listen(server);

io.on('connection', socket => {
   console.log('a user connected');
   socket.on('chat message', (msg) => {
      io.emit('chat message', msg);
   });
});

server.listen(port, () => {
   console.log('server on!');
});
