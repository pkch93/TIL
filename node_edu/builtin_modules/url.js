const url = require('url');
const URL = url.URL;

const test1 = new URL('https://example.org/hello?abc=123');
console.log(test1);
console.log(test1.protocol);
console.log(test1.hash);
console.log(test1.username);
console.log(test1.password);
console.log(test1.host);
console.log(test1.href);
console.log(test1.origin);
console.log(test1.pathname);
console.log(test1.searchParams.get('abc'));
test1.searchParams.append('def', '234');
console.log(test1.href);
console.log(test1.searchParams.toString())

console.log('======================================')

const test2 = url.parse('https://example.org/hello?abc=123');
console.log(test2)
const qs = require('querystring');
const query = test2.query;
const parsingQuery = qs.parse(query);
console.log(parsingQuery['abc']);
console.log(typeof(parsingQuery));
parsingQuery['bcd'] = '234';
console.log(test2.href);
test3 = url.parse(test2.href + '&bdc=234');
console.log(test3);
console.log(test3.href);