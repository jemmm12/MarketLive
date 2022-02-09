const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = (app) => {
    app.use(
        createProxyMiddleware('/user', {
            target: 'http://3.38.250.70:8110',
            changeOrigin: true
        })
    )
    app.use(
        createProxyMiddleware('/auth', {
            target: 'http://3.38.250.70:8110',
            changeOrigin: true
        })
    )
    app.use(
        createProxyMiddleware('/dm', {
            target: 'http://3.38.250.70:8111',
            changeOrigin: true
        })
    )
};