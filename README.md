# Woolbattle

A competitive Minecraft gamemode plugin where teams battle using wool blocks and special perks. Players use bows, special abilities, and strategic building to eliminate opponents and claim victory.

## 🎮 What is Woolbattle?

Woolbattle is a fast-paced team-based PvP gamemode for Minecraft where players:
- Fight in teams using bows and special perks
- Build and destroy using wool blocks
- Use unique abilities like grappling hooks, boosters, and explosives
- Compete in dynamic maps with void-based elimination

## 📋 Features

- **Team-based gameplay** with customizable team sizes
- **20+ unique perks** including active and passive abilities
- **Lobby system** with countdown and player management
- **Map voting system** with schematic-based maps
- **Player statistics** with optional MongoDB integration
- **Spectator mode** for eliminated players
- **Custom scoreboard** with real-time updates
- **Particle effects** and visual enhancements

## 🛠️ Requirements

- **Minecraft Version**: 1.21.8 (Paper)
- **Java**: 21 or higher
- **Dependencies**:
  - FastAsyncWorldEdit
- **Optional**:
  - MongoDB (for player data persistence)

## 📦 Installation

1. Download the latest release from the [Releases](../../releases) page
2. Place the `.jar` file in your server's `plugins` folder
3. Install FastAsyncWorldEdit plugin
4. Start your server to generate the configuration files
5. Configure the plugin (see Configuration section)
6. Restart your server

## ⚙️ Configuration

Edit `plugins/Woolbattle/config.yml`:

```yaml
Debug: false           # Enable debug mode
MongoDB: true          # Enable/disable database persistence
LobbyCountdown: 30     # Lobby countdown in seconds
TeamSize: 2            # Players per team
Teams: 2               # Number of teams
PlayersToStartCountdown: 2  # Minimum players to start countdown
MaxPlayers: 4          # Maximum server capacity
```

### MongoDB Setup (Optional)

If you want to persist player data across server restarts:

1. Install and run MongoDB on your server
2. Set `MongoDB: true` in config.yml
3. The plugin will automatically connect to `mongodb://localhost:27017`

To disable database functionality:
- Set `MongoDB: false` in config.yml
- Player perks will be stored in memory only

## 🎯 Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/start` | Start the game manually | `woolbattle.admin` |
| `/countdown <seconds>` | Set lobby countdown | `woolbattle.admin` |
| `/debug` | Toggle debug mode | `woolbattle.admin` |
| `/build` | Toggle build mode | `woolbattle.admin` |
| `/performance` | View server performance | `woolbattle.admin` |
| `/switchteam` | Switch teams | `woolbattle.player` |
| `/all <message>` | Broadcast message | `woolbattle.admin` |

## 🏗️ Building from Source

1. Clone the repository:
```bash
git clone https://github.com/yourusername/Woolbattle.git
cd Woolbattle
```

2. Build the plugin:
```bash
./gradlew build
```

3. The compiled JAR will be in `build/libs/`

## 🎮 Gameplay

### Lobby Phase
- Players join and select teams using lobby items
- Minimum players required to start countdown
- Players can select perks and configure settings
- Vote for maps (if multiple available)

### Game Phase
- Teams spawn on opposite sides of the map
- Use bows and perks to eliminate opponents
- Build with wool blocks to create bridges and defenses
- Avoid the void - falling means elimination
- Last team standing wins!

## 🔧 Development

### Project Structure
```
src/main/java/codes/Elix/Woolbattle/
├── main/           # Main plugin class
├── game/           # Game mechanics and perks
├── gamestates/     # Game state management
├── listeners/      # Event listeners
├── items/          # Game items and GUI
├── util/           # Utilities and helpers
└── commands/       # Command implementations
```

### Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📝 TODO
### Generell
- [x] Buildprotection for Lobby
- [x] Foodlevelchange mit Doublejump
- [x] Doublejump based on walk vector
- [x] Void Death System
    - [x] LebenSystem
    - [x] VoidTeleport
    - [x] Spectator



### GAMEPLAY
#### LobbyItems
- [x] Bow (Perks)
- [x] Book (Team Wählen)
- [x] Lohenrute (Partikel sind an)
- [ ] Chest (Inventarsortierung)
- [x] Nametag (Lebensanzahl ändern)
- [ ] Paper (Maps)

#### PERKS
- [x] Tauscher
- [x] Booster
- [x] Enterhaken
- [x] Freezer
- [x] Großvaters Uhr
- [ ] Linebuilder
- [ ] Minigun
- [x] Woolbombe
- [ ] Pfeilbombe
- [x] Rettungskapsel
- [x] Wandgenerator
- [ ] Klospülung
- [ ] Sprengsatz
- [ ] SlimePlattform
- [ ] Schutzschild
- [x] Rope
- [x] Rettungsplatform
- [ ] The Grabber

#### PASSIVE PERKS
- [x] Aufzug
- [x] Explodierender Pfeil
- [ ] Teleport Pfeil
- [ ] Pfeilregen
- [x] Recharger
- [ ] Reflector
- [x] Rocket Jump
- [ ] Schock Pfeil
- [ ] SlowArrow
- [ ] Spinne
- [ ] Stomper
- [ ] Woolinventory

## 🐛 Known Issues

- None currently reported

## 📊 Statistics

Track player performance with optional MongoDB integration:
- Games played
- Wins/losses
- Favorite perks
- Kill/death ratios

## 🤝 Support

- **Issues**: Report bugs or request features in the [Issues](../../issues) section
- **Discord**: Join our community server (link here)
- **Wiki**: Check the [Wiki](../../wiki) for detailed guides

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Authors

- **Elix** - *Lead Developer*
- **N4med** - *Co-Developer*

## 🙏 Acknowledgments

- FastAsyncWorldEdit team for world editing capabilities
- Paper team for the excellent server software
- MongoDB team for database solutions
- Community contributors and testers

## 📈 Version History

### v1.0 (Latest)
- Initial release
- Full gamemode implementation
- MongoDB integration
- 20+ perks system
- Team-based gameplay
