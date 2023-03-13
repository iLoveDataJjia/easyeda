import { BrowserRouter as RouterProvider, Routes, Route } from "react-router-dom";
import { LoginPg, RegisterPg, AppPg } from "./pages";
import { CtxProvider, ToasterCpt } from "./context";
import { HttpProvider } from "./hooks";

/**
 * Application entrypoint.
 */
function App() {
  return (
    <div className="flex min-h-screen flex-col bg-slate-900">
      <HttpProvider>
        <CtxProvider>
          <RouterProvider>
            <Routes>
              <Route path="/login" element={<LoginPg />} />
              <Route path="/register" element={<RegisterPg />} />
              <Route path="/app/*" element={<AppPg />} />
            </Routes>
            <ToasterCpt />
          </RouterProvider>
        </CtxProvider>
      </HttpProvider>
    </div>
  );
}

export default App;
