const express = require('express');
const { createServer } = require('node:http')
const { Server } = require('socket.io');
const cors = require('cors');

const PORT = 3001;
const service = require('./service/messageService');

require("dotenv").config();

const app = express();
app.use(cors());
app.use('/messages', require('./routes'));

const server = createServer(app);
const io = new Server(server, {
    cors: {
        origin: `http://${process.env.PUBLIC_IP}, http://${process.env.CORS_ORIGIN}, http://${process.env.PUBLIC_IP2}`,
        methods: ["GET", "POST", "PATCH", "DELETE"],
        allowedHeaders: ["Origin", "X-Requested-With", "Content-Type", "Accept", "Authorization"],
        credentials: true
    }
});

io.on('connection', socket => {
    const teams = socket.handshake.auth.teams;
    const user = socket.handshake.auth.user;

    teams.forEach(team => {
        socket.join(team);

        io.to(team).emit('ttm', user.id);

        socket.on('ttm', m => {
            const message = {
                message: m.content,
                date: m.date,
                userId: m.user.id,
                teamId: m.team.id,
                username: m.user.username
            };

            try {
                service.registerMessage(message);
            } catch (e) {
                return socket.emit('messageError', e);
            }

            io.to(team).emit('ttm', message);
        });
    });

    socket.on('disconnected', () => {
        console.log('user disconnected');
    });
});

server.listen(PORT, () => {
    console.log(`Running at port ${PORT}`);
});