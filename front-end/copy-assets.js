import fs from 'fs';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Copy CSS files
const cssFiles = [
  'base.css',
  'style.css', 
  'responsive.css',
  'font-awesome.css'
];

const cssSourceDir = path.join(__dirname, '..', 'trizzy', 'css');
const cssDestDir = path.join(__dirname, 'src', 'assets', 'css');

// Create destination directory if it doesn't exist
if (!fs.existsSync(cssDestDir)) {
  fs.mkdirSync(cssDestDir, { recursive: true });
}

// Copy CSS files
cssFiles.forEach(file => {
  const sourcePath = path.join(cssSourceDir, file);
  const destPath = path.join(cssDestDir, file);
  
  if (fs.existsSync(sourcePath)) {
    fs.copyFileSync(sourcePath, destPath);
    console.log(`Copied ${file}`);
  } else {
    console.log(`File not found: ${sourcePath}`);
  }
});

// Copy colors directory
const colorsSourceDir = path.join(__dirname, '..', 'trizzy', 'css', 'colors');
const colorsDestDir = path.join(__dirname, 'src', 'assets', 'css', 'colors');

if (!fs.existsSync(colorsDestDir)) {
  fs.mkdirSync(colorsDestDir, { recursive: true });
}

if (fs.existsSync(colorsSourceDir)) {
  const colorFiles = fs.readdirSync(colorsSourceDir);
  colorFiles.forEach(file => {
    const sourcePath = path.join(colorsSourceDir, file);
    const destPath = path.join(colorsDestDir, file);
    fs.copyFileSync(sourcePath, destPath);
    console.log(`Copied color: ${file}`);
  });
}

// Copy images directory
const imagesSourceDir = path.join(__dirname, '..', 'trizzy', 'images');
const imagesDestDir = path.join(__dirname, 'src', 'assets', 'images');

if (!fs.existsSync(imagesDestDir)) {
  fs.mkdirSync(imagesDestDir, { recursive: true });
}

if (fs.existsSync(imagesSourceDir)) {
  const copyImagesRecursive = (source, dest) => {
    const items = fs.readdirSync(source);
    items.forEach(item => {
      const sourcePath = path.join(source, item);
      const destPath = path.join(dest, item);
      
      if (fs.statSync(sourcePath).isDirectory()) {
        if (!fs.existsSync(destPath)) {
          fs.mkdirSync(destPath, { recursive: true });
        }
        copyImagesRecursive(sourcePath, destPath);
      } else {
        fs.copyFileSync(sourcePath, destPath);
        console.log(`Copied image: ${item}`);
      }
    });
  };
  
  copyImagesRecursive(imagesSourceDir, imagesDestDir);
}

console.log('Asset copying completed!'); 