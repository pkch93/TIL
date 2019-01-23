const path = require('path');

console.log(path.sep);
console.log(path.delimiter);
console.log(path.dirname('./path.js'));
console.log(path.extname('./path.js'));
console.log(path.basename('.'));
console.log(__filename);
console.log(__dirname);
const temp = path.parse('../../Day1.md');
console.log(path.format(temp));
console.log(path.format({
    dir: '\\',
    base: 'file.txt',
}));
console.log(path.relative('../../../January', 'Day01.md'))
console.log(path.isAbsolute('.'))
console.log(path.isAbsolute('/'))
console.log(path.join('wwwroot', 'dar', '/ads'))
