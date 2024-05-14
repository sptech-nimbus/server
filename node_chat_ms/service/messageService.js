const ObjectId = require('mongodb').ObjectId;
const mongodb = require('../database/mongodb');

module.exports.registerMessage = async message => {
    const db = await mongodb.getCollection('messages');

    return await db.insertOne(message);
}

module.exports.findByTeamIdPageable = async (teamId, page, elements) => {
    const db = await mongodb.getCollection('messages');

    const result = await db.aggregate([{
        $facet: {
            page: [
                { $skip: (Number(page) - 1) * Number(elements) },
                { $limit: Number(elements) },
                { $match: { teamId } }
            ],
            totalCount: [
                { $count: 'total' }
            ]
        }
    },
    {
        $project: {
            page: 1,
            total: { $arrayElemAt: ['$totalCount.total', 0] }
        }
    }
    ]).toArray();

    return result[0];
}