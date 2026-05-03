db = db.getSiblingDB('order');

db.createUser({
    user: 'order_user',
    pwd: 'test123',
    roles: [
        {
            role: 'readWrite',
            db: 'order'
        }
    ]
});