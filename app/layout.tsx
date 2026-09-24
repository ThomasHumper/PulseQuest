import "./globals.css";

export const metadata = {
  title: "PulseQuest",
  description: "Turn everyday life into a game."
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}