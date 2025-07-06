# CutLayout Frontend

React-based frontend application converted from Trizzy HTML template using Vite.

## Features

- 🎨 Modern React components with hooks
- 📱 Responsive design
- 🎯 SEO optimized with React Helmet
- 🚀 Fast development with Vite
- 🎪 Smooth animations with Framer Motion
- 📊 Bundle analysis support
- 🎨 Prettier code formatting

## Quick Start

### Prerequisites

- Node.js (v16 or higher)
- npm or yarn

### Installation

1. Install dependencies:
```bash
npm install
```

2. Copy assets from template:
```bash
npm run copy-assets
```

3. Start development server:
```bash
npm run dev
```

4. Open [http://localhost:5173](http://localhost:5173) in your browser.

## Available Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint
- `npm run format` - Format code with Prettier
- `npm run analyze` - Analyze bundle size
- `npm run copy-assets` - Copy assets from template

## Project Structure

```
src/
├── components/          # Reusable React components
│   ├── Navbar.jsx      # Navigation component
│   ├── Footer.jsx      # Footer component
│   ├── FeaturedSection.jsx
│   ├── NewArrivals.jsx
│   ├── ParallaxBanner.jsx
│   ├── ProductLists.jsx
│   └── LatestArticles.jsx
├── pages/              # Page components
│   ├── HomePage.jsx    # Main homepage
│   ├── LoginPage.jsx
│   ├── RegisterPage.jsx
│   ├── AdminPage.jsx
│   └── UserPage.jsx
├── assets/             # Static assets
│   ├── css/           # Stylesheets
│   └── images/        # Images
└── api/               # API utilities
    └── auth.js
```

## Components Overview

### HomePage
Main homepage that combines all sections:
- Hero slider with Swiper
- Featured products section
- New arrivals carousel
- Parallax banner
- Product lists (Best Sellers, Top Rated, Weekly Sales)
- Latest articles
- Footer

### Key Features

1. **Responsive Design**: Mobile-first approach with breakpoints
2. **Performance**: Optimized images and lazy loading
3. **SEO**: Meta tags and structured data
4. **Accessibility**: ARIA labels and keyboard navigation
5. **Animations**: Smooth transitions and hover effects

## Styling

The project uses a combination of:
- Original Trizzy CSS (imported via main.css)
- Custom React-specific styles
- Responsive design patterns
- Modern CSS features (Grid, Flexbox, CSS Variables)

## Dependencies

### Core
- React 18.2.0
- React Router DOM 6.20.0
- Vite 4.5.0

### UI & Animation
- Swiper 11.0.0 (for carousels)
- Framer Motion 10.16.0 (animations)
- React Icons 4.12.0

### Utilities
- Axios 1.6.0 (HTTP client)
- React Helmet Async 1.3.0 (SEO)
- React Intersection Observer 9.5.0 (scroll effects)

## Development

### Code Style
- ESLint for linting
- Prettier for formatting
- React Hooks best practices

### Performance
- Bundle analysis available
- Code splitting with React.lazy()
- Image optimization
- Tree shaking enabled

## Deployment

1. Build the project:
```bash
npm run build
```

2. The built files will be in the `dist/` directory

3. Deploy to your preferred hosting service (Netlify, Vercel, etc.)

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and linting
5. Submit a pull request

## License

This project is licensed under the MIT License. 