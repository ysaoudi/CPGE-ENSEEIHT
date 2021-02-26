const yargs = require("yargs")
const notes = require("./notes.js")

yargs.version("1.0.0")

// Add Command
yargs.command({
    command :'add',
    describe :'Add a new note',
    builder:{
        title: {
            describe: 'Note Title',
            demandOption: true,
            type: 'string'
        },
        body: {
            describe: 'Note Body',
            demandOption : true,
            type: 'string'
        }
    },
    handler(argv) {
        notes.addNote(argv.title, argv.body)
    }
})

// Remove Command
yargs.command({
    command :'remove',
    describe :'remove existing note',
    builder:{
        title: {
            describe: 'Note Title',
            demandOption: true,
            type: 'string'
        }
    },
    handler(argv) {
        notes.removeNote(argv.title)
    }
})

// List Command
yargs.command({
    command :'list',
    describe :'list all commands',
    handler(){ notes.listNotes()}
})

// Read Command
yargs.command({
    command :'read',
    describe :'read existing note',
    builder:{
        title: {
            describe: 'Note Title',
            demandOption: true,
            type: 'string'
        }
    },
    handler(argv){notes.readNote(argv.title)}
})

yargs.parse()