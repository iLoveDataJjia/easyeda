import { SideBar } from "./components/SideBar";
import "./App.css";
import { BrowserRouter } from "react-router-dom";
import { AnimatedRoutes } from "./components/AnimatedRoutes";
import { Connectors } from "./components/Connectors";
import { ProfilInfo } from "./components/ProfilInfo";
import { DataFrameModal } from "./components/DataFrameModal";
import { useState } from "react";

function App() {
  // Modal logic
  const [isDataFrameModal, setDataFrameModal] = useState(false);
  const isAnyModal = isDataFrameModal;

  // Render
  return (
    <div className="App flex select-none bg-gray-800">
      <BrowserRouter>
        {/* SideBar */}
        <SideBar />

        {/* Page */}
        <div className="relative flex flex-1 flex-col">
          {/* Header */}
          <div className="flex items-center justify-around p-10">
            <Connectors modalSetter={setDataFrameModal} />
            <ProfilInfo />
          </div>

          {/* Content */}
          <AnimatedRoutes />

          {/* Modal */}
          {isAnyModal && (
            <div className="absolute inset-0 m-auto h-5/6 w-5/6 rounded-lg bg-gray-900 drop-shadow-md">
              {isDataFrameModal && <DataFrameModal modalSetter={setDataFrameModal} />}
            </div>
          )}
        </div>
      </BrowserRouter>
    </div>
  );
}

export default App;
