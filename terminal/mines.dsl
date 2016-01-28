c = new GameConsole()

def newGame = { width, height, bombs ->
    c.newGame(width, height, bombs)
    c.status()
}

def open = { x, y ->
    c.open x, y
    c.status()
}

def flag = { x, y ->
    c.flag x, y
    c.status()
}

def status = {
    c.status()
}

def auto = {
    c.auto()
    c.status()
}

def robot = { delayInMs=400 ->
    while (c.game.isRunning()) {
        auto()
        sleep(delayInMs)
    }
}

n = newGame
o = open
f = flag
s = status

status()
