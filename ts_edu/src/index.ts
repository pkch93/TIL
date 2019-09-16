import test from '@src/test';

const START_NUMBER = 2;
const LAST_NUMBER = 9;

for (let i = START_NUMBER; i <= LAST_NUMBER; i += 1) {
  for (let j = 1; j <= LAST_NUMBER; j += 1) {
    console.log(`${i} x ${j} = ${i * j}`);
  }
}

test();
