const fs = require("fs")
const {logWarning, logError} =  require("./utils.js")


const addNote = (title, body) => {
    const notes = loadNotes()
    const duplicateNote = notes.find((n) => n.title === title)
    
    if(duplicateNote) {
        logError("Note " + title + " ALREADY EXISTS !")
        return
    }

    notes.push({
        title: title,
        body: body
    })
    saveNotes(notes)
    logWarning("Adding new note : \n\t\tTitle: " + title + "\n\t\tBody: " + body)
}

const removeNote = (title) => {
    const notes = loadNotes()
    const differentNotes = notes.filter((n) => n.title !== title)

    if(differentNotes.length === 0  || differentNotes.length === notes.length) {
        logError("Note " + title + " NOT FOUND !")
        return
    }

    saveNotes(differentNotes)
    logWarning("Removing note : " + title)
}

const listNotes = () => {
    const notes = loadNotes()

    if(notes.length === 0) {
        logError("No Notes Added Yet!")
        return
    }

    logWarning("Listing all notes:\n")

    notes.forEach( (n) => {
        logWarning("Title: " + n.title + "\n\t Body: " + n.body + "\n")
    })
}

const readNote = (title) => {
    const note = loadNotes().find((n) => n.title === title)
    if(!note){
        logError("No Note Found")
        return
    }
    logWarning("Found note:\n")
    logWarning("Title: " + note.title + "\n\t Body: " + note.body + "\n")
}

const loadNotes = () => {
    try{
        return JSON.parse(fs.readFileSync("notes.json").toString())
    } catch (e) {
        return []
    }
}

const saveNotes = (notesArray) => {
    fs.writeFileSync("notes.json", JSON.stringify(notesArray))
}

module.exports = {
    addNote: addNote,
    removeNote: removeNote,
    listNotes: listNotes,
    readNote:readNote
}