import axios from 'axios';
import { Fake } from './fake.js';

const BASE_URL = 'http://127.0.0.1:8080';

let token = '';

class RestClient {
    async login(email, pass) {
        let resp;
        try {
            resp = await axios.post(`${BASE_URL}/account.login`, {
                email: email,
                password: pass
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data;
    }

    async getUsers() {
        let resp;
        try {
            resp = await axios.get(`${BASE_URL}/users.list?size=1000&sort=email,desc`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data.content;
    }

    async createUser(user) {
        let resp;
        try {
            resp = await axios.post(`${BASE_URL}/users.create`, user, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data;
    }

    async modifyUser(id, user) {
        let resp;
        try {
            resp = await axios.put(`${BASE_URL}/users.modify/${id}`, user, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data;
    }

    async createNote(note) {
        let resp;
        try {
            resp = await axios.post(`${BASE_URL}/notes.create`, note, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data;
    }

    async getCategories() {
        let resp;
        try {
            resp = await axios.get(`${BASE_URL}/categories.list?size=100`, {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data.content;
    }

    async addShare(noteId, share) {
        let resp;
        try {
            resp = await axios.post(`${BASE_URL}/shared.create/${noteId}`, share, {
                headers: {
                    Authorization: `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });
        } catch (err) {
            console.error(`ERROR: ${JSON.stringify(err.response.data)}`);
            process.exit();
        }
        return resp?.data;
    }
}

const Roles = {
    ADMIN: 'ADMIN',
    USER: 'USER',
};

const States = {
    PUBLIC: 'PUBLIC',
    PRIVATE: 'PRIVATE',
}

const Access = {
    RW: 'RW',
    RO: 'RO',
}

class GeneratorApp {

    rest = new RestClient;

    *parseArgs(args) {
        let opt, arg;
        args.shift(); /* remove node command */
        args.shift(); /* remove script name */
        for (let data of args) {
            if (data.substring(0, 1) == '-') {
                opt = data.substring(1, 2);
                arg = data.substring(2);
                yield [opt, arg];
            } else {
                yield ['', data];
            }
        }
    }

    die(message) {
        console.error(`ERROR: ${message}`);
        process.exit();
    }

    async generateUsers() {
        const fake = new Fake;

        const randomUsers = await fake.randomUsers();
        const oneRandomUser = () => {
            return randomUsers[~~(Math.random() * randomUsers.length)];
        }

        for (let i = 0; i < 10; ++i) {
            const u = oneRandomUser();
            const user = {
                email: u['email'],
                fullName: `${u['first_name']} ${u['last_name']}`,
                password: 123,
                roles: [Roles.USER],
            }

            const response = await this.rest.createUser(user);
            response.locked = Math.random() > 0.5 ? true : false;
            await this.rest.modifyUser(response.id, response);
        }
    }

    async generateNotes() {
        const fake = new Fake;

        const categories = await this.rest.getCategories();
        const oneRandomCategory = () => {
            return categories[~~(Math.random() * categories.length)];
        }

        const users = await this.rest.getUsers();
        const oneRandomUser = () => {
            return users[~~(Math.random() * users.length)];
        }

        const oneRandomAccess = () => {
            return Math.random() > 0.5 ? Access.RW : Access.RO;
        }

        for (let i = 0; i < 10; ++i) {
            const note = {
                caption: fake.generateCaption(),
                body: fake.generateBody(),
                state: States.PUBLIC,
                category: oneRandomCategory().name,
            }

            const response = await this.rest.createNote(note);
            const u = oneRandomUser();
            const share = {
                userEmail: u.email,
                access: oneRandomAccess(),
            }

            await this.rest.addShare(response.id, share);
        }
    }

    async run(args) {
        let user, pass, command;
        if (args.length > 2) {
            for (let [opt, arg] of this.parseArgs(args)) {
                switch (opt) {
                    default:
                        this.die(`Option not recognized "${opt}"`);
                    case 'u':
                        user = arg;
                        break;
                    case 'p':
                        pass = arg;
                        break;
                    case '':
                        command = arg;
                        break;
                }
            }
        }

        if (!command) {
            this.die("No command specified");
        }

        const response = await this.rest.login('sam@example.com', '123');
        token = response.token;

        switch (command) {
            default:
                this.die(`Command not recodnized "${command}"`);
            case 'users':
                await this.generateUsers();
                break;
            case 'notes':
                await this.generateNotes();
                break;
        }
    }
}

const a = new GeneratorApp;
a.run(process.argv);
