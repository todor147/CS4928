# UML Diagram Generation Instructions

## 📊 Where to Generate the UML Diagram

You have several excellent **FREE** online options to generate the UML diagram from the `UML_DIAGRAM.puml` file:

---

## ⭐ RECOMMENDED: PlantUML Online Editors

### Option 1: **PlantText** (Easiest - No account needed)
**Website:** https://www.planttext.com/

**Steps:**
1. Go to https://www.planttext.com/
2. Clear the default text in the editor
3. Copy and paste the **entire contents** of `UML_DIAGRAM.puml`
4. The diagram will auto-generate on the right side
5. Click **"Download"** to save as PNG or SVG

**Pros:**
- ✅ No account needed
- ✅ Simple interface
- ✅ Instant preview
- ✅ Download as PNG/SVG

---

### Option 2: **PlantUML Web Server** (Official)
**Website:** http://www.plantuml.com/plantuml/uml/

**Steps:**
1. Go to http://www.plantuml.com/plantuml/uml/
2. Paste your PlantUML code
3. Click "Submit"
4. View and download the diagram

**Pros:**
- ✅ Official PlantUML server
- ✅ Multiple export formats (PNG, SVG, PDF)
- ✅ Reliable and fast

---

### Option 3: **PlantUML QEditor** (Feature-rich)
**Website:** https://plantuml-editor.kkeisuke.com/

**Steps:**
1. Go to https://plantuml-editor.kkeisuke.com/
2. Paste your code in the left panel
3. See live preview on the right
4. Export as PNG/SVG

**Pros:**
- ✅ Split-screen editor
- ✅ Syntax highlighting
- ✅ Live preview
- ✅ Multiple export options

---

### Option 4: **Visual Studio Code Extension** (If using VS Code)
**Extension:** PlantUML by jebbs

**Steps:**
1. Install the "PlantUML" extension in VS Code
2. Open `UML_DIAGRAM.puml`
3. Press `Alt + D` to preview
4. Right-click and export to PNG/SVG

**Pros:**
- ✅ Works offline
- ✅ Integrated with your IDE
- ✅ Multiple export formats

---

## 🚀 Quick Start (Fastest Method)

**For immediate results:**

1. **Go to:** https://www.planttext.com/
2. **Open:** `Week05/UML_DIAGRAM.puml` in a text editor
3. **Copy:** All the content (Ctrl+A, Ctrl+C)
4. **Paste:** Into PlantText editor
5. **Download:** Click the download button (PNG or SVG)

---

## 📥 Alternative: Use Online Converter

**Website:** https://kroki.io/

**Steps:**
1. Go to https://kroki.io/
2. Select "PlantUML" as diagram type
3. Paste your code
4. Download as PNG/SVG

---

## 💾 Saving the Diagram

Once generated, save the image as:
```
Week05/UML_DIAGRAM.png
```
or
```
Week05/UML_DIAGRAM.svg
```

SVG is recommended for better quality in documentation!

---

## 📝 What's in the Diagram

The UML diagram includes:

### Interfaces (6)
- Product
- Priced
- Catalog
- PaymentStrategy
- OrderObserver
- OrderPublisher

### Core Classes (5)
- Money
- SimpleProduct
- LineItem
- Order
- InMemoryCatalog
- OrderIds

### Decorator Pattern (5)
- ProductDecorator (abstract)
- ExtraShot
- OatMilk
- Syrup
- SizeLarge

### Factory Pattern (1)
- ProductFactory

### Strategy Pattern (3)
- CardPayment
- CashPayment
- WalletPayment

### Relationships Shown
- Inheritance (implements/extends)
- Composition (has-a)
- Dependencies (uses)
- Aggregation
- Pattern annotations

---

## 🎨 Customization

If you want to customize the diagram:

1. Edit `UML_DIAGRAM.puml`
2. Modify colors in the `skinparam` section:
   ```
   skinparam classBackgroundColor #YOUR_COLOR
   skinparam classBorderColor #YOUR_COLOR
   ```
3. Add/remove classes or relationships as needed
4. Regenerate using any of the above methods

---

## 🔧 Troubleshooting

**Diagram doesn't render?**
- Make sure you copied the ENTIRE content
- Check for missing `@startuml` at the top
- Check for missing `@enduml` at the bottom

**Want different layout?**
- PlantUML auto-layouts, but you can add:
  ```
  left to right direction
  ```
  after `@startuml` for horizontal layout

**Export quality issues?**
- Use SVG format for scalable graphics
- SVG can be converted to any size PNG later

---

## 📚 Additional Resources

- **PlantUML Documentation:** https://plantuml.com/
- **PlantUML Class Diagram Guide:** https://plantuml.com/class-diagram
- **PlantUML Cheat Sheet:** https://ogom.github.io/draw_uml/plantuml/

---

## ✅ Verification

Your diagram should show:
- All 4 design patterns clearly labeled
- Inheritance relationships with hollow arrows
- Composition with filled diamonds
- Dependencies with dashed arrows
- All classes with their methods and attributes

---

**Recommended for submission:** PlantText or PlantUML Web Server
**Format:** PNG or SVG (SVG preferred for quality)
**Include in deliverables:** Both the .puml source and the generated image

Happy diagramming! 🎨

