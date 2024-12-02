const MongoClient = require('mongodb').MongoClient;

module.exports.getCollection = async collection => {
    const client = await MongoClient.connect(process.env.MONGO_URL || process.env.MONGODB_URI);

    const col = client.db(process.env.MONGO_DATABASE).collection(collection);
    
    return col;
}