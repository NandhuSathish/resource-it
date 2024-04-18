import { sample } from 'lodash';
import { faker } from '@faker-js/faker';

// ----------------------------------------------------------------------

export const users = [...Array(24)].map(() => ({
  id: faker.string.uuid(),
  employeeId: faker.number.int(1000),
  department: sample(['Software', 'Hr Department', 'Tesing', 'Finance']),
  name: faker.person.fullName(),
  project: faker.company.name(),
  band: sample(['A', 'B', 'C', 'D']),
  skill: sample([
    {
      name: 'UI Designer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
    {
      name: 'UX Designer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
    {
      name: 'UI/UX Designer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
    {
      name: 'Backend Developer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
    {
      name: 'Full Stack Designer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
    {
      name: 'Front End Developer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
    {
      name: 'Full Stack Developer',
      experience: faker.number.int(10),
      level: sample(['Expert', 'Intermediate', 'Beginner']),
    },
  ]),
  role: sample(['Manager', 'Resource', 'HOD', 'HR']),
  experiance: faker.number.int(10),
  benchDays: faker.number.int(100),
  status: sample(['active', 'banned']),
}));
