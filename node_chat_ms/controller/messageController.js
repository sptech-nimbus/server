const service = require('../service/messageService');

module.exports.findByTeamIdPageable = async (req, res) => {
    let messagesPage = await service.findByTeamIdPageable(req.params.teamId, req.query.page, req.query.elements);
    
    return res.status(200).send(messagesPage);
}