const express = require('express');
const router = express.Router();
const controller = require('./controller/messageController');

router.get('/:teamId', (req, res) => {
    return controller.findByTeamIdPageable(req, res);
});

module.exports = router;