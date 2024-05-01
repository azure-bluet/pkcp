record_to_key = {
    "W": "keyUp",
    "A": "keyLeft",
    "S": "keyDown",
    "D": "keyRight",
    "JUMP": "keyJump",
    "SPRINT": "keySprint",
    "SNEAK": "keyShift",
    "LMB": "keyAttack",
    "RMB": "keyUse",
}

for k in record_to_key:
    print (f"    options.{record_to_key [k]}.setDown (macro.{k});")

for c in "WASDJCsLR":
    print (f'                this.keys.indexOf ("{c}") != -1,')
