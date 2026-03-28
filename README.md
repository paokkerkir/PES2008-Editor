# PES2008 PS2 Editor - Import from PES6 OF

An extended version of the PES2008 PS2 Editor (based on [Compulsion's editor - GPLv3 licenced](http://www.purplehaze.eclipse.co.uk)) with support for loading PES6 PC Option Files (OF2 format) and converting them to PES2008.

---

## Features

- **Import player from OF2 (PES6)** - appearance, stats, and special abilities
- **Mass import all players** with configurable options
- **Import Kits / Team Names / Formations** with options
- **Export/Import Teams & Players** to/from files - cross-compatible with PES6 Editor by FABIO VITOR; supports multi-person collaboration on the same option file
- **Edit appearance data** in player card (including shirt and sleeves)
- **Export PES2008 DB** - fixes the 5000 ML salary bug
- **Names mass edit** - lowercase / UPPERCASE / Title Case and auto-fix spacing errors
- **Skin color and face type** in the player panel
- **Import only ML Young** from PES2008 OF2
- **Shirt Untucked** checkbox and **Sleeves** dropdown in player card
- **Both options** available in mass adjustments

---

## Known Limitations

| Area | Status |
|---|---|
| Import PES6 Logos, emblems, league names, stadium names, boots | ❌ Not supported |
| Import PES6 Special hairstyles | ⚠️ Hit or miss - verify in-game |

---

## Notes

### Mass Import
- Mass copy imports players only up to **ID 4413**, preserving PES2008 PES Shop, ML Old/Young, and ML Default slots. PES6 unused slots will not overwrite PES2008 rosters.
- The **last 8 teams** in PES2008 (which have no PES6 equivalent) keep their original players as long as their ID is above 4413. You can import a full team over them afterward.
- It is recommended to use a **default PES2008 OF** (or one with unedited Other Teams C) as a base for mass importing, to avoid players being registered in both the imported squad and one of the extra 8 teams. Not game-breaking — you can release them from the extra team — but a clean base is preferable.

### Nationalities
- Nationalities import correctly in general.
- **Italy** is mapped correctly.
- **Israel NT** in PES2008 will copy **Latvia NT** from PES6 during mass import. Israeli players from PES6 remain Israeli; Latvian players will appear as Israeli. This cannot be changed during conversion but can be fixed manually afterward.
- Nationalities that exist in PES6 but not in PES2008 will be converted to **No Nationality**.

### Face Types
- If a player receives the **ORIGINAL** face type when importing from PES6, change it manually. Leaving it as ORIGINAL has a high probability of crashing the game when accessing that player.

### Emblems & Logos
- When importing teams, **revert emblems to default** (right-click) manually, otherwise emblem bugs will occur.
- If logos are imported in the same order as in PES6, technical sponsors and logos will work correctly.
- Use **PESFan Editor 6.0.1** to extract emblems/logos from a PES6 OF, then import them normally with this editor. Note that PES2008 has fewer slots than PES6.

---

## Changelog

### v9.5 — 17 Feb 2026
- Fixed Export PES2008 DB (fixes 5000 ML Salary bug)

### v9.4
- Added skin color and face type in the player panel
- Fixed mass name edits stopping before ML player IDs
- Added option to import only ML Young from PES2008 OF2
- Added "For Team Only" in mass adjustments

### v9.3
- Added names mass edit
- Fixed mass import menu when loading PES6 OF
- Fixed mass import logic
- Added more options in mass import for Other Teams C import control
- Added Export PES2008 DB (experimental)
- Added Shirt Untucked checkbox and Sleeves dropdown in player card
- Added both options in mass adjustments


---


## Usage

Java Runtime Environment needed.
Run start.bat

## Source

Source code is included. Contributions and improvements are welcome.
