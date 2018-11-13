var _a = require('./connection'), client = _a.client, index = _a.index, type = _a.type;
module.exports = {
    /** Query ES index for the provided term */
    queryTerm: function (term, offset) {
        if (offset === void 0) { offset = 0; }
        var body = {
            from: offset,
            query: { match: {
                    text: {
                        query: term,
                        operator: 'and',
                        fuzziness: 'auto'
                    }
                } },
            highlight: { fields: { text: {} } }
        };
        return client.search({ index: index, type: type, body: body });
    }
};
