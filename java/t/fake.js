import axios from "axios";
import { LoremIpsum } from "lorem-ipsum";

const lorem = new LoremIpsum({
    sentencesPerParagraph: {
        max: 8,
        min: 4
    },
    wordsPerSentence: {
        max: 16,
        min: 4
    }
});

export class Fake {

    getRandomArbitrary(min, max) {
        return Math.random() * (max - min) + min;
    }

    generateCaption() {
        return lorem.generateWords(1) + this.getRandomArbitrary(0, 1_000_000);
    }

    generateBody() {
        return lorem.generateParagraphs(1);
    }

    async randomUsers() {
        const url = "https://random-data-api.com/api/v2/users?size=100";
        const response = await axios.get(url).then(resp => {
            return resp.data;
        });
        return response;
    }
}

